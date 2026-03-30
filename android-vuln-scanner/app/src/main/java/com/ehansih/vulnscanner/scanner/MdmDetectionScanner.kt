package com.ehansih.vulnscanner.scanner

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ehansih.vulnscanner.data.models.AppLogger
import com.ehansih.vulnscanner.data.models.DeviceAdminEntry
import com.ehansih.vulnscanner.data.models.MdmDetectionResult
import com.ehansih.vulnscanner.data.models.SecurityFinding
import com.ehansih.vulnscanner.data.models.Severity

class MdmDetectionScanner(private val context: Context) {

    companion object {
        // Well-known MDM package names mapped to their vendor display name
        private val KNOWN_MDM_PACKAGES = mapOf(
            "com.microsoft.intune"                        to "Microsoft Intune",
            "com.microsoft.launcher.enterprise"           to "Microsoft Intune (Managed Home Screen)",
            "com.microsoft.intune.mam"                    to "Microsoft Intune MAM",
            "com.airwatch.androidagent"                   to "VMware Workspace ONE (AirWatch)",
            "com.air.android.mdm"                         to "VMware Workspace ONE",
            "com.vmware.boxer"                            to "VMware Boxer",
            "com.mobileiron.android"                      to "Ivanti MobileIron",
            "com.mobileiron.anyware.android"              to "MobileIron Go",
            "com.zenprise"                                to "Citrix Endpoint Management (XenMobile)",
            "com.citrix.mdx"                              to "Citrix MDX",
            "com.citrix.work"                             to "Citrix Workspace",
            "com.blackberry.uem.client"                   to "BlackBerry UEM",
            "com.good.android.gfe"                        to "BlackBerry Dynamics",
            "com.jamf.connect"                            to "Jamf Connect",
            "com.jamf.protect"                            to "Jamf Protect",
            "com.sap.afaria"                              to "SAP Mobile Secure",
            "com.sophos.smsec"                            to "Sophos Mobile",
            "com.symantec.secureweb"                      to "Symantec Mobile Management",
            "com.tangoe.mdm"                              to "Tangoe Mobile",
            "com.zebra.devicetracker"                     to "Zebra Technologies MDM",
            "com.miradore.online"                         to "Miradore MDM",
            "com.hexnode.hexnodemdm"                      to "Hexnode MDM",
            "com.scalefusion.mdm.client.android"          to "Scalefusion MDM",
            "com.suremdm.suremdm"                         to "SureMDM",
            "com.ninjaone.mdm"                            to "NinjaRMM",
            "com.google.android.apps.work.clouddpc"       to "Android Enterprise (Google Cloud DPC)"
        )
    }

    fun scan(): MdmDetectionResult {
        AppLogger.i("MdmDetectionScanner", "Starting MDM/corporate monitoring detection scan")

        val dpm       = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val pm        = context.packageManager
        val findings  = mutableListOf<SecurityFinding>()

        val activeAdmins    = getActiveAdmins(dpm)
        val deviceOwner     = getDeviceOwner(dpm)
        val hasWorkProfile  = checkWorkProfile(dpm)
        val isFullyManaged  = deviceOwner != null

        val adminApps: List<DeviceAdminEntry> = buildAdminApps(pm, activeAdmins)

        // Device owner findings
        if (isFullyManaged) {
            findings.add(SecurityFinding(
                title          = "Device is Fully Managed (MDM Device Owner: $deviceOwner)",
                detail         = "A Device Owner (DO) app has full administrative control over this device. " +
                                 "The owning organisation can remotely wipe the device, install/remove apps, " +
                                 "enforce policies, monitor all activity, and prevent factory resets. " +
                                 "Device owner: $deviceOwner",
                severity       = Severity.HIGH,
                recommendation = "If this device was issued by your employer, this is expected. " +
                                 "If it is a personal device, contact your IT department or consider " +
                                 "a factory reset after backing up personal data."
            ))
        }

        // Work profile findings
        if (hasWorkProfile && !isFullyManaged) {
            findings.add(SecurityFinding(
                title          = "Work Profile Active",
                detail         = "A work profile is installed on this device. Your employer can manage and " +
                                 "monitor apps within the work profile, but cannot see your personal data.",
                severity       = Severity.LOW,
                recommendation = "This is expected if you have enrolled a corporate account. " +
                                 "Personal apps and data remain private. You can remove the work profile in " +
                                 "Settings → Accounts → Work profile → Remove work profile."
            ))
        }

        // Suspicious admin apps (non-MDM apps with admin rights)
        val suspiciousAdmins = adminApps.filter { it.vendor == null }
        if (suspiciousAdmins.isNotEmpty()) {
            findings.add(SecurityFinding(
                title          = "Unrecognised Apps with Device Admin Privileges",
                detail         = "${suspiciousAdmins.size} app(s) have Device Administrator rights but are not " +
                                 "recognised as legitimate MDM solutions: " +
                                 suspiciousAdmins.joinToString { "${it.appName} (${it.packageName})" },
                severity       = Severity.HIGH,
                recommendation = "Settings → Security → Device admin apps. Review and deactivate any admin " +
                                 "app you did not intentionally install. These can prevent uninstallation of " +
                                 "stalkerware and ransomware."
            ))
        }

        // Known MDM apps info
        val knownMdmApps = adminApps.filter { it.vendor != null }
        if (knownMdmApps.isNotEmpty()) {
            findings.add(SecurityFinding(
                title          = "Corporate MDM Software Detected (${knownMdmApps.size} app(s))",
                detail         = "The following known MDM software is installed with admin privileges: " +
                                 knownMdmApps.joinToString { "${it.vendor} (${it.packageName})" } +
                                 ". Your device activity may be monitored by your organisation.",
                severity       = Severity.MEDIUM,
                recommendation = "If this is a corporate device, this is expected. If personal, check with " +
                                 "your IT department about what data is collected. You can review MDM policies " +
                                 "in Settings → Device admin apps."
            ))
        }

        AppLogger.i(
            "MdmDetectionScanner",
            "Scan complete — deviceOwner=$deviceOwner workProfile=$hasWorkProfile " +
            "admins=${adminApps.size} knownMdm=${knownMdmApps.size}"
        )

        return MdmDetectionResult(
            isManaged           = isFullyManaged || adminApps.isNotEmpty(),
            hasWorkProfile      = hasWorkProfile,
            deviceOwnerPackage  = deviceOwner,
            deviceAdmins        = adminApps,
            findings            = findings
        )
    }

    // ── Active admins ─────────────────────────────────────────────────────────

    private fun getActiveAdmins(dpm: DevicePolicyManager): List<android.content.ComponentName> {
        return runCatching {
            dpm.activeAdmins ?: emptyList()
        }.getOrElse { e ->
            AppLogger.e("MdmDetectionScanner", "Error getting active admins: ${e.message}", e)
            emptyList()
        }
    }

    // ── Device owner ──────────────────────────────────────────────────────────

    private fun getDeviceOwner(dpm: DevicePolicyManager): String? {
        return runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (dpm.isDeviceOwnerApp(context.packageName)) {
                    context.packageName
                } else {
                    // DevicePolicyManager.isDeviceManaged() is a system-only API.
                    // Detect managed state via active admin presence as a proxy.
                    null
                }
            } else null
        }.getOrElse { e ->
            AppLogger.e("MdmDetectionScanner", "Error checking device owner: ${e.message}", e)
            null
        }
    }

    // ── Work profile ──────────────────────────────────────────────────────────

    private fun checkWorkProfile(@Suppress("UNUSED_PARAMETER") dpm: DevicePolicyManager): Boolean {
        return runCatching {
            // UserManager.isManagedProfile() does not require admin privileges (API 24+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val um = context.getSystemService(android.os.UserManager::class.java)
                um?.isManagedProfile ?: false
            } else false
        }.getOrElse { e ->
            AppLogger.d("MdmDetectionScanner", "Work profile check: ${e.message}")
            false
        }
    }

    // ── Build MdmApp list ─────────────────────────────────────────────────────

    private fun buildAdminApps(
        pm: PackageManager,
        admins: List<android.content.ComponentName>
    ): List<DeviceAdminEntry> {
        return admins.map { component ->
            val packageName = component.packageName
            val appName = runCatching {
                pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString()
            }.getOrDefault(packageName)

            val vendorName = KNOWN_MDM_PACKAGES[packageName]
            AppLogger.d("MdmDetectionScanner", "Admin app: $packageName ($appName) isKnownMdm=${vendorName != null}")

            DeviceAdminEntry(
                packageName = packageName,
                appName     = appName,
                vendor      = vendorName
            )
        }
    }
}
