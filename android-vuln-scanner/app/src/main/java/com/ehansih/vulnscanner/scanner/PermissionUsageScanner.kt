package com.ehansih.vulnscanner.scanner

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import com.ehansih.vulnscanner.data.models.AppLogger
import com.ehansih.vulnscanner.data.models.PermissionUsageEntry
import com.ehansih.vulnscanner.data.models.PermissionUsageResult
import com.ehansih.vulnscanner.data.models.SecurityFinding
import com.ehansih.vulnscanner.data.models.Severity

class PermissionUsageScanner(private val context: Context) {

    companion object {
        // Each entry: displayName, opStr (AppOpsManager.OPSTR_*), Android permission string
        private val OPS_OF_INTEREST = listOf(
            Triple("Camera",    AppOpsManager.OPSTR_CAMERA,       "android.permission.CAMERA"),
            Triple("Microphone",AppOpsManager.OPSTR_RECORD_AUDIO, "android.permission.RECORD_AUDIO"),
            Triple("Location",  AppOpsManager.OPSTR_FINE_LOCATION,"android.permission.ACCESS_FINE_LOCATION"),
            Triple("Contacts",  AppOpsManager.OPSTR_READ_CONTACTS,"android.permission.READ_CONTACTS"),
            Triple("SMS",       AppOpsManager.OPSTR_READ_SMS,     "android.permission.READ_SMS")
        )
    }

    fun scan(): PermissionUsageResult {
        AppLogger.i("PermissionUsageScanner", "Starting permission usage scan (API ${Build.VERSION.SDK_INT})")

        val entries  = mutableListOf<PermissionUsageEntry>()
        val findings = mutableListOf<SecurityFinding>()
        val appOps   = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val pm       = context.packageManager

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

        // Only scan user-installed + updated system apps
        val userPackages = packages.filter { pkg ->
            val ai = pkg.applicationInfo ?: return@filter false
            val isSystem = (ai.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            val isUpdated = (ai.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
            !isSystem || isUpdated
        }

        AppLogger.i("PermissionUsageScanner", "Checking ${userPackages.size} packages for sensitive permission usage")

        for (pkg in userPackages) {
            val packageName = pkg.packageName
            val uid = runCatching { pm.getApplicationInfo(packageName, 0).uid }.getOrNull() ?: continue

            for ((displayName, opStr, androidPermission) in OPS_OF_INTEREST) {
                try {
                    // Use unsafeCheckOpNoThrow (public API 29+) for more accurate op-level check
                    // Fall back to checkPermission for older APIs
                    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        appOps.unsafeCheckOpNoThrow(opStr, uid, packageName)
                    } else {
                        if (pm.checkPermission(androidPermission, packageName) ==
                            PackageManager.PERMISSION_GRANTED)
                            AppOpsManager.MODE_ALLOWED
                        else
                            AppOpsManager.MODE_ERRORED
                    }
                    if (mode != AppOpsManager.MODE_ALLOWED) continue

                    val appName = runCatching {
                        pm.getApplicationLabel(pkg.applicationInfo!!).toString()
                    }.getOrDefault(packageName)

                    val severity = when (displayName) {
                        "Camera", "Microphone" -> Severity.HIGH
                        "Location"             -> Severity.MEDIUM
                        else                   -> Severity.LOW
                    }

                    entries.add(PermissionUsageEntry(
                        packageName        = packageName,
                        appName            = appName,
                        permission         = displayName,
                        lastAccessed       = System.currentTimeMillis(),
                        isBackgroundAccess = false,
                        severity           = severity
                    ))
                } catch (_: Exception) { /* skip if op check fails */ }
            }
        }

        if (entries.isNotEmpty()) {
            val camMicApps = entries.filter { it.permission in listOf("Camera", "Microphone") }
            if (camMicApps.isNotEmpty()) {
                findings.add(SecurityFinding(
                    title          = "Sensitive Permissions Granted (${camMicApps.size} apps)",
                    detail         = "Apps with Camera or Microphone access: " +
                                     camMicApps.distinctBy { it.packageName }
                                               .take(5)
                                               .joinToString { it.appName },
                    severity       = Severity.MEDIUM,
                    recommendation = "Settings → Apps → [App] → Permissions to review and revoke any " +
                                     "camera/microphone access you did not intentionally grant."
                ))
            }
        }

        val totalApps = entries.map { it.packageName }.toSet().size
        AppLogger.i("PermissionUsageScanner", "Scan complete — $totalApps apps with sensitive permissions granted")

        return PermissionUsageResult(
            entries                   = entries,
            backgroundAccessApps      = emptyList(),
            totalAppsWithRecentAccess = totalApps,
            findings                  = findings
        )
    }
}
