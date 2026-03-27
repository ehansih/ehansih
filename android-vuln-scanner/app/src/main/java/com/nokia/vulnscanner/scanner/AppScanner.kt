package com.nokia.vulnscanner.scanner

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.nokia.vulnscanner.data.api.NvdApi
import com.nokia.vulnscanner.data.api.bestCvssScore
import com.nokia.vulnscanner.data.api.englishDescription
import com.nokia.vulnscanner.data.db.CveDao
import com.nokia.vulnscanner.data.models.*
import kotlinx.coroutines.delay

class AppScanner(
    private val context: Context,
    private val nvdApi: NvdApi,
    private val cveDao: CveDao
) {
    // Permissions considered high-risk on Android
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

    // Known sideload installer package names
    private val trustedInstallers = setOf(
        "com.android.vending",          // Google Play Store
        "com.amazon.venezia",           // Amazon App Store
        "com.huawei.appmarket",
        "com.samsung.android.packageinstaller",
        "com.oppo.market",              // OnePlus / OPPO store
        "com.nearme.romupdate",         // OxygenOS
    )

    suspend fun scanInstalledApps(
        onProgress: (current: Int, total: Int, appName: String) -> Unit
    ): List<AppScanResult> {
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

        // Skip system packages that have no APK on data partition
        val userPackages = packages.filter { it.applicationInfo != null }

        val results = mutableListOf<AppScanResult>()
        userPackages.forEachIndexed { idx, pkg ->
            val appName = pm.getApplicationLabel(pkg.applicationInfo!!).toString()
            onProgress(idx + 1, userPackages.size, appName)
            results.add(scanSingleApp(pm, pkg, appName))
            // Respect NVD rate limit — 5 req/s unauthenticated, be conservative
            delay(250)
        }
        return results
    }

    private suspend fun scanSingleApp(
        pm: PackageManager,
        pkg: PackageInfo,
        appName: String
    ): AppScanResult {
        val packageName = pkg.packageName

        // ── Permissions ──────────────────────────────────────────────────────
        val perms = (pkg.requestedPermissions ?: emptyArray()).mapIndexed { i, perm ->
            val granted = pkg.requestedPermissionsFlags?.getOrNull(i)
                ?.and(PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
            PermissionEntry(
                name       = perm,
                group      = perm.substringAfterLast('.'),
                isDangerous = perm in dangerousPermissions,
                isGranted  = granted
            )
        }

        // ── Install source ────────────────────────────────────────────────────
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

        // ── CVE lookup ────────────────────────────────────────────────────────
        val cves = lookupCves(appName, packageName)

        // ── Risk scoring ──────────────────────────────────────────────────────
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
            packageName  = packageName,
            appName      = appName,
            versionName  = pkg.versionName ?: "unknown",
            versionCode  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pkg.longVersionCode else pkg.versionCode.toLong(),
            installedFrom = installSource,
            permissions  = perms,
            cves         = cves,
            riskScore    = riskScore.coerceAtMost(100),
            flags        = flags
        )
    }

    private suspend fun lookupCves(appName: String, packageName: String): List<CveRecord> {
        // Use app name as keyword; fall back to package suffix
        val keyword = appName.take(30)

        // Check local cache first
        val cached = cveDao.getByProduct(keyword)
        if (cached.isNotEmpty()) return cached

        return try {
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
            records
        } catch (e: Exception) {
            android.util.Log.e("AppScanner", "CVE lookup failed for '$keyword'", e)
            emptyList()
        }
    }
}
