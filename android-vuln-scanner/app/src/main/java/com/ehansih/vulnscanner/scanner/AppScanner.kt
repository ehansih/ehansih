package com.ehansih.vulnscanner.scanner

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.ehansih.vulnscanner.data.api.NvdApi
import com.ehansih.vulnscanner.data.api.bestCvssScore
import com.ehansih.vulnscanner.data.api.englishDescription
import com.ehansih.vulnscanner.data.db.CveDao
import com.ehansih.vulnscanner.data.models.*
import com.ehansih.vulnscanner.data.models.AppLogger
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.net.UnknownHostException

class AppScanner(
    private val context: Context,
    private val nvdApi: NvdApi,
    private val cveDao: CveDao
) {
    // Set to false on first DNS failure — skips all subsequent CVE lookups that session
    @Volatile private var nvdReachable = true

    // Exposed after scan so ScanOrchestrator can include in ScanSummary
    val wasNvdReachable: Boolean get() = nvdReachable

    private val dangerousPermissions = setOf(
        "android.permission.READ_CONTACTS",
        "android.permission.WRITE_CONTACTS",
        "android.permission.READ_CALL_LOG",
        "android.permission.WRITE_CALL_LOG",
        "android.permission.PROCESS_OUTGOING_CALLS",
        "android.permission.READ_SMS",
        "android.permission.RECEIVE_SMS",
        "android.permission.SEND_SMS",
        "android.permission.READ_PHONE_STATE",
        "android.permission.CALL_PHONE",
        "android.permission.CAMERA",
        "android.permission.RECORD_AUDIO",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.MANAGE_EXTERNAL_STORAGE",
        "android.permission.GET_ACCOUNTS",
        "android.permission.USE_BIOMETRIC",
        "android.permission.USE_FINGERPRINT",
        "android.permission.BODY_SENSORS",
        "android.permission.ACTIVITY_RECOGNITION",
        "android.permission.BLUETOOTH_SCAN",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.UWB_RANGING",
        "android.permission.READ_MEDIA_IMAGES",
        "android.permission.READ_MEDIA_VIDEO",
        "android.permission.READ_MEDIA_AUDIO"
    )

    private val trustedInstallers = setOf(
        "com.android.vending",
        "com.amazon.venezia",
        "com.huawei.appmarket",
        "com.samsung.android.packageinstaller",
        "com.oppo.market",
        "com.nearme.romupdate",
    )

    suspend fun scanInstalledApps(
        onProgress: suspend (current: Int, total: Int, appName: String) -> Unit
    ): List<AppScanResult> {
        nvdReachable = true  // reset for each new scan

        val pm = context.packageManager
        val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(
                    (PackageManager.GET_PERMISSIONS or PackageManager.GET_META_DATA).toLong()
                )
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getInstalledPackages(PackageManager.GET_PERMISSIONS or PackageManager.GET_META_DATA)
        }

        val userPackages = packages.filter { it.applicationInfo != null }
        AppLogger.i("AppScanner", "Scanning ${userPackages.size} installed packages")

        val results = mutableListOf<AppScanResult>()
        userPackages.forEachIndexed { idx, pkg ->
            val appName = pm.getApplicationLabel(pkg.applicationInfo!!).toString()
            onProgress(idx + 1, userPackages.size, appName)
            AppLogger.d("AppScanner", "[${idx+1}/${userPackages.size}] Scanning: $appName")
            results.add(scanSingleApp(pm, pkg, appName))
            // 1500ms delay — safely within NVD unauthenticated limit of 5 req/30s
            delay(1500L)
        }

        val cveStatus = if (nvdReachable) "CVE lookups OK" else "CVE lookups SKIPPED (NVD unreachable — corporate/MDM network may be blocking services.nvd.nist.gov)"
        AppLogger.i("AppScanner", "App scan complete — ${results.count { it.riskScore > 0 }} risky apps. $cveStatus")
        return results
    }

    private suspend fun scanSingleApp(
        pm: PackageManager,
        pkg: PackageInfo,
        appName: String
    ): AppScanResult {
        val packageName = pkg.packageName

        val perms = (pkg.requestedPermissions ?: emptyArray()).mapIndexed { i, perm ->
            val granted = pkg.requestedPermissionsFlags?.getOrNull(i)
                ?.and(PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
            PermissionEntry(
                name        = perm,
                group       = perm.substringAfterLast('.'),
                isDangerous = perm in dangerousPermissions,
                isGranted   = granted
            )
        }

        val installer = runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                pm.getInstallSourceInfo(packageName).installingPackageName ?: "Unknown"
            } else {
                @Suppress("DEPRECATION")
                pm.getInstallerPackageName(packageName) ?: "Unknown"
            }
        }.getOrDefault("Unknown")

        val installSource = when {
            installer in trustedInstallers -> "Official Store"
            installer == "Unknown" || installer == "null" -> "Sideloaded / Unknown"
            else -> installer
        }

        val cves = lookupCves(appName, packageName)

        val flags = mutableListOf<String>()
        var riskScore = 0

        val dangerousGranted = perms.count { it.isDangerous && it.isGranted }
        if (dangerousGranted >= 5) { flags.add("$dangerousGranted dangerous permissions granted"); riskScore += 20 }
        else if (dangerousGranted >= 2) { riskScore += 10 }

        if (installSource.contains("Sideloaded")) { flags.add("Installed outside app store"); riskScore += 25 }

        cves.forEach { cve ->
            riskScore += when (cve.severity) {
                Severity.CRITICAL -> 30
                Severity.HIGH     -> 20
                Severity.MEDIUM   -> 10
                Severity.LOW      -> 5
                Severity.INFO     -> 0
            }
            flags.add("CVE ${cve.cveId} (CVSS ${cve.cvssScore})")
        }

        return AppScanResult(
            packageName   = packageName,
            appName       = appName,
            versionName   = pkg.versionName ?: "unknown",
            versionCode   = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pkg.longVersionCode else pkg.versionCode.toLong(),
            installedFrom = installSource,
            permissions   = perms,
            cves          = cves,
            riskScore     = riskScore.coerceAtMost(100),
            flags         = flags
        )
    }

    private suspend fun lookupCves(appName: String, packageName: String): List<CveRecord> {
        val keyword = appName.take(30)

        val cached = cveDao.getByProduct(keyword)
        if (cached.isNotEmpty()) return cached

        // Skip all NVD calls once we know the network/DNS is blocking it
        if (!nvdReachable) return emptyList()

        var backoffMs = 2000L
        for (attempt in 1..3) {
            try {
                val response = nvdApi.searchCves(keyword = keyword, resultsPerPage = 10)
                val records = response.vulnerabilities.map { item ->
                    val cve   = item.cve
                    val score = cve.bestCvssScore()
                    CveRecord(
                        cveId           = cve.id,
                        description     = cve.englishDescription(),
                        cvssScore       = score,
                        severity        = cvssToSeverity(score),
                        publishedDate   = cve.published,
                        affectedProduct = keyword,
                        references      = cve.references.joinToString(",") { it.url }
                    )
                }
                if (records.isNotEmpty()) cveDao.insertAll(records)
                return records
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    AppLogger.w("AppScanner", "NVD rate limited (429), backing off ${backoffMs}ms (attempt $attempt/3)")
                    delay(backoffMs)
                    backoffMs *= 2  // exponential: 2s → 4s → 8s
                } else {
                    AppLogger.e("AppScanner", "CVE lookup HTTP ${e.code()} for '$keyword'")
                    return emptyList()
                }
            } catch (e: UnknownHostException) {
                AppLogger.w("AppScanner", "NVD DNS unreachable — disabling CVE lookups for this scan session")
                nvdReachable = false
                return emptyList()
            } catch (e: Exception) {
                AppLogger.e("AppScanner", "CVE lookup failed for '$keyword': ${e.message}", e)
                return emptyList()
            }
        }
        return emptyList()
    }
}
