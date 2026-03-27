package com.nokia.vulnscanner.scanner

import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.provider.Settings
import com.nokia.vulnscanner.data.api.NvdApi
import com.nokia.vulnscanner.data.api.bestCvssScore
import com.nokia.vulnscanner.data.api.englishDescription
import com.nokia.vulnscanner.data.db.CveDao
import com.nokia.vulnscanner.data.models.*
import java.io.File

class DeviceSecurityScanner(
    private val context: Context,
    private val nvdApi: NvdApi,
    private val cveDao: CveDao
) {
    suspend fun scan(): DeviceSecurityResult {
        val isRooted           = checkRoot()
        val isDevMode          = checkDeveloperMode()
        val isUsbDebug         = checkUsbDebugging()
        val unknownSources     = checkUnknownSources()
        val screenLock         = checkScreenLock()
        val encrypted          = checkEncryption()
        val biometric          = checkBiometric()
        val verifyApps         = checkVerifyApps()
        val patchLevel         = Build.VERSION.SECURITY_PATCH
        val androidVer         = Build.VERSION.RELEASE

        val osVulns = lookupOsCves(androidVer, patchLevel)
        val findings = mutableListOf<SecurityFinding>()

        if (isRooted) findings.add(SecurityFinding(
            title          = "Device is Rooted",
            detail         = "Root access detected via su binary and known root paths.",
            severity       = Severity.CRITICAL,
            recommendation = "Avoid using rooted devices for personal banking or corporate access. " +
                             "Consider re-flashing stock firmware."
        ))

        if (isDevMode) findings.add(SecurityFinding(
            title          = "Developer Mode Enabled",
            detail         = "Developer options are active. This exposes additional attack surface.",
            severity       = Severity.HIGH,
            recommendation = "Go to Settings → About Phone → tap Build Number 7× to toggle. " +
                             "Disable in Settings → Developer options → Off."
        ))

        if (isUsbDebug) findings.add(SecurityFinding(
            title          = "USB Debugging Active",
            detail         = "ADB over USB is enabled — attacker with physical access can dump data.",
            severity       = Severity.HIGH,
            recommendation = "Disable in Settings → Developer options → USB debugging → Off."
        ))

        if (unknownSources) findings.add(SecurityFinding(
            title          = "Unknown App Sources Allowed",
            detail         = "Apps can be installed from outside the official store.",
            severity       = Severity.MEDIUM,
            recommendation = "Settings → Apps → Special app access → Install unknown apps. " +
                             "Revoke permission for any app that doesn't need it."
        ))

        if (!screenLock) findings.add(SecurityFinding(
            title          = "No Screen Lock",
            detail         = "Device has no PIN, pattern, password, or biometric lock.",
            severity       = Severity.CRITICAL,
            recommendation = "Settings → Security → Screen lock → choose PIN or stronger."
        ))

        if (!encrypted) findings.add(SecurityFinding(
            title          = "Storage Not Encrypted",
            detail         = "Device storage may not be fully encrypted.",
            severity       = Severity.HIGH,
            recommendation = "Settings → Security → Encryption & credentials → Encrypt phone."
        ))

        if (!biometric) findings.add(SecurityFinding(
            title          = "Biometric Not Configured",
            detail         = "No fingerprint or face unlock enrolled.",
            severity       = Severity.LOW,
            recommendation = "Settings → Security → Fingerprint → enroll a fingerprint for quick secure unlock."
        ))

        if (!verifyApps) findings.add(SecurityFinding(
            title          = "Verify Apps Disabled",
            detail         = "Google Play Protect app scanning is off.",
            severity       = Severity.HIGH,
            recommendation = "Open Play Store → Profile icon → Play Protect → Settings → Scan apps with Play Protect → Enable."
        ))

        // Old patch level check
        val patchDate = runCatching {
            val parts = patchLevel.split("-")
            val year  = parts[0].toInt()
            val month = parts[1].toInt()
            val curYear  = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            val curMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
            ((curYear - year) * 12) + (curMonth - month)
        }.getOrDefault(0)

        if (patchDate > 6) findings.add(SecurityFinding(
            title          = "Security Patch Outdated ($patchLevel)",
            detail         = "Your device security patches are $patchDate months old.",
            severity       = if (patchDate > 12) Severity.CRITICAL else Severity.HIGH,
            recommendation = "Settings → System → Software update → check for updates. " +
                             "If OEM stopped updates, consider upgrading device or flashing a community ROM."
        ))

        val secScore = calculateDeviceScore(isRooted, isDevMode, isUsbDebug, unknownSources,
                                            screenLock, encrypted, biometric, verifyApps, patchDate, osVulns)

        return DeviceSecurityResult(
            androidVersion         = androidVer,
            securityPatchLevel     = patchLevel,
            isRooted               = isRooted,
            isDeveloperModeEnabled = isDevMode,
            isUsbDebuggingEnabled  = isUsbDebug,
            isUnknownSourcesAllowed = unknownSources,
            isScreenLockEnabled    = screenLock,
            isDeviceEncrypted      = encrypted,
            isBiometricEnabled     = biometric,
            isVerifyAppsEnabled    = verifyApps,
            osVulnerabilities      = osVulns,
            securityScore          = secScore,
            findings               = findings
        )
    }

    // ── Individual checks ─────────────────────────────────────────────────────

    private fun checkRoot(): Boolean {
        val rootPaths = listOf(
            "/system/app/Superuser.apk",
            "/sbin/su", "/system/bin/su", "/system/xbin/su",
            "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su",
            "/data/local/su", "/su/bin/su",
            "/system/xbin/busybox", "/system/bin/busybox"
        )
        if (rootPaths.any { File(it).exists() }) return true
        return try {
            val p = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            val out = p.inputStream.bufferedReader().readText()
            p.destroy()
            out.contains("uid=0")
        } catch (_: Exception) { false }
    }

    private fun checkDeveloperMode(): Boolean =
        Settings.Global.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0

    private fun checkUsbDebugging(): Boolean =
        Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) != 0

    private fun checkUnknownSources(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26+: per-app install permission; we flag if ANY non-store app has it
            // We check the legacy setting as a proxy
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.INSTALL_NON_MARKET_APPS, 0) != 0
        } else {
            @Suppress("DEPRECATION")
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.INSTALL_NON_MARKET_APPS, 0) != 0
        }
    }

    private fun checkScreenLock(): Boolean {
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return km.isDeviceSecure
    }

    private fun checkEncryption(): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return dpm.storageEncryptionStatus in listOf(
            DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE,
            DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE_PER_USER,
            DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE_DEFAULT_KEY
        )
    }

    private fun checkBiometric(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val bm = context.getSystemService(BiometricManager::class.java)
            bm?.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
                    BiometricManager.BIOMETRIC_SUCCESS
        } else {
            @Suppress("DEPRECATION")
            android.hardware.fingerprint.FingerprintManager.isHardwareDetected?.let { false } ?: false
        }
    }

    private fun checkVerifyApps(): Boolean =
        Settings.Global.getInt(context.contentResolver, "package_verifier_enable", 1) != 0

    // ── OS CVE lookup ─────────────────────────────────────────────────────────

    private suspend fun lookupOsCves(androidVersion: String, patchLevel: String): List<CveRecord> {
        val keyword = "android $androidVersion"
        val cached  = cveDao.getByProduct(keyword)
        if (cached.isNotEmpty()) return cached

        return try {
            val response = nvdApi.searchCves(keyword = keyword, resultsPerPage = 15)
            val records  = response.vulnerabilities.map { item ->
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
        } catch (_: Exception) { emptyList() }
    }

    // ── Score ─────────────────────────────────────────────────────────────────

    private fun calculateDeviceScore(
        rooted: Boolean, devMode: Boolean, usbDebug: Boolean, unknownSrc: Boolean,
        screenLock: Boolean, encrypted: Boolean, biometric: Boolean, verifyApps: Boolean,
        patchAge: Int, osVulns: List<CveRecord>
    ): Int {
        var score = 100
        if (rooted)       score -= 40
        if (devMode)      score -= 15
        if (usbDebug)     score -= 15
        if (unknownSrc)   score -= 10
        if (!screenLock)  score -= 30
        if (!encrypted)   score -= 20
        if (!biometric)   score -= 5
        if (!verifyApps)  score -= 15
        score -= (patchAge * 2).coerceAtMost(20)
        osVulns.forEach { cve ->
            score -= when (cve.severity) {
                Severity.CRITICAL -> 10
                Severity.HIGH     -> 5
                Severity.MEDIUM   -> 2
                else              -> 1
            }
        }
        return score.coerceIn(0, 100)
    }
}
