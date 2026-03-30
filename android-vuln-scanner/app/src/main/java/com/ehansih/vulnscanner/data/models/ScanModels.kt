package com.ehansih.vulnscanner.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// ── Severity ─────────────────────────────────────────────────────────────────

enum class Severity { CRITICAL, HIGH, MEDIUM, LOW, INFO }

fun cvssToSeverity(cvss: Double): Severity = when {
    cvss >= 9.0 -> Severity.CRITICAL
    cvss >= 7.0 -> Severity.HIGH
    cvss >= 4.0 -> Severity.MEDIUM
    cvss > 0.0  -> Severity.LOW
    else        -> Severity.INFO
}

// ── CVE record (Room entity for local cache) ──────────────────────────────────

@Entity(tableName = "cve_cache")
data class CveRecord(
    @PrimaryKey val cveId: String,
    val description: String,
    val cvssScore: Double,
    val severity: Severity,
    val publishedDate: String,
    val affectedProduct: String,
    val references: String
)

// ── App scan result ──────────────────────────────────────────────────────────

data class AppScanResult(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val versionCode: Long,
    val installedFrom: String,
    val permissions: List<PermissionEntry>,
    val cves: List<CveRecord>,
    val riskScore: Int,
    val flags: List<String>
)

data class PermissionEntry(
    val name: String,
    val group: String,
    val isDangerous: Boolean,
    val isGranted: Boolean
)

// ── OS / device security result ───────────────────────────────────────────────

data class DeviceSecurityResult(
    val androidVersion: String,
    val securityPatchLevel: String,
    val isRooted: Boolean,
    val isDeveloperModeEnabled: Boolean,
    val isUsbDebuggingEnabled: Boolean,
    val isUnknownSourcesAllowed: Boolean,
    val isScreenLockEnabled: Boolean,
    val isDeviceEncrypted: Boolean,
    val isBiometricEnabled: Boolean,
    val isVerifyAppsEnabled: Boolean,
    val osVulnerabilities: List<CveRecord>,
    val securityScore: Int,
    val findings: List<SecurityFinding>
)

data class SecurityFinding(
    val title: String,
    val detail: String,
    val severity: Severity,
    val recommendation: String
)

// ── Network security result ───────────────────────────────────────────────────

data class NetworkSecurityResult(
    val ssid: String,
    val bssid: String,
    val securityType: String,
    val isVpnActive: Boolean,
    val isProxySet: Boolean,
    val tlsVersion: String,
    val findings: List<SecurityFinding>
)

// ── Attack Intelligence ───────────────────────────────────────────────────────

enum class AttackType(val label: String) {
    BANKING_TROJAN("Banking Trojan"),
    SPYWARE("Spyware"),
    RANSOMWARE("Ransomware"),
    DATA_THEFT("Data Theft"),
    SUBSCRIPTION_FRAUD("Subscription Fraud"),
    REMOTE_ACCESS("Remote Access Trojan"),
    STALKERWARE("Stalkerware")
}

data class AttackIncident(
    val name: String,
    val year: String,
    val attackType: AttackType,
    val affectedDevices: String,
    val financialImpact: String,
    val financialImpactValue: Long,     // USD value for aggregation
    val dataBreachImpact: String,
    val description: String,
    val relevantTrigger: String,        // key used to match against scan triggers
    val severity: Severity
)

data class AttackIntelligence(
    val relevantAttacks: List<AttackIncident>,
    val totalFinancialExposure: String, // formatted, e.g. "$465M+"
    val criticalAttackCount: Int,
    val riskSummary: String
)

// ── Malware Behavior ─────────────────────────────────────────────────────────

data class SuspiciousApp(
    val packageName: String,
    val appName: String,
    val reason: String,
    val severity: Severity
)

data class MalwareBehaviorResult(
    val accessibilityServiceAbusers: List<SuspiciousApp>,
    val deviceAdmins: List<SuspiciousApp>,
    val overlayApps: List<SuspiciousApp>,
    val hiddenApps: List<SuspiciousApp>,
    val riskScore: Int = 0,
    val findings: List<SecurityFinding> = emptyList()
) {
    val totalSuspicious: Int get() =
        accessibilityServiceAbusers.size + deviceAdmins.size + overlayApps.size + hiddenApps.size
    val categoriesFound: Int get() = listOf(
        accessibilityServiceAbusers, deviceAdmins, overlayApps, hiddenApps
    ).count { it.isNotEmpty() }
}

// ── Permission Usage ──────────────────────────────────────────────────────────

data class PermissionUsageEntry(
    val packageName: String,
    val appName: String,
    val permission: String,
    val lastAccessed: Long,
    val isBackgroundAccess: Boolean,
    val severity: Severity
)

data class PermissionUsageResult(
    val entries: List<PermissionUsageEntry>,
    val backgroundAccessApps: List<PermissionUsageEntry>,
    val totalAppsWithRecentAccess: Int,
    val findings: List<SecurityFinding>
)

// ── Breach Monitor ────────────────────────────────────────────────────────────

data class BreachRecord(
    val name: String,
    val domain: String = "",
    val breachDate: String,
    val dataClasses: List<String>,
    val description: String,
    val isVerified: Boolean
)

data class VirusTotalResult(
    val packageName: String,
    val appName: String,
    val sha256: String,
    val maliciousCount: Int,
    val suspiciousCount: Int = 0,
    val totalEngines: Int,
    val detectionNames: List<String>
) {
    val isFlagged: Boolean get() = maliciousCount > 0
}

// ── MDM Detection ─────────────────────────────────────────────────────────────

data class DeviceAdminEntry(
    val packageName: String,
    val appName: String,
    val vendor: String?
)

data class MdmDetectionResult(
    val isManaged: Boolean,
    val deviceOwnerPackage: String?,
    val hasWorkProfile: Boolean,
    val deviceAdmins: List<DeviceAdminEntry>,
    val findings: List<SecurityFinding> = emptyList()
)

// ── SIM Security ──────────────────────────────────────────────────────────────

data class SimSecurityResult(
    val simState: String,
    val operatorName: String,
    val countryIso: String,
    val networkType: String,
    val isRoaming: Boolean,
    val isOnLegacyNetwork: Boolean,
    val phoneCount: Int = 1,
    val findings: List<SecurityFinding> = emptyList(),
    val riskScore: Int = 0
)

// ── Bluetooth ─────────────────────────────────────────────────────────────────

data class BluetoothDeviceInfo(
    val name: String,
    val address: String,
    val deviceClass: String,
    val bondState: String
)

data class BluetoothScanResult(
    val isEnabled: Boolean,
    val isDiscoverable: Boolean,
    val pairedDevices: List<BluetoothDeviceInfo>,
    val findings: List<SecurityFinding>,
    val riskScore: Int
)

// ── Full scan summary ─────────────────────────────────────────────────────────

data class ScanSummary(
    val scanTime: Long = System.currentTimeMillis(),
    val totalApps: Int,
    val vulnerableApps: Int,
    val criticalCves: Int,
    val highCves: Int,
    val deviceScore: Int,
    val overallRisk: Severity,
    val appResults: List<AppScanResult>,
    val deviceResult: DeviceSecurityResult,
    val networkResult: NetworkSecurityResult?,
    val cveNetworkAvailable: Boolean = true,
    val attackIntelligence: AttackIntelligence? = null,
    val malwareBehaviorResult: MalwareBehaviorResult? = null,
    val permissionUsageResult: PermissionUsageResult? = null,
    val bluetoothResult: BluetoothScanResult? = null,
    val simResult: SimSecurityResult? = null,
    val mdmResult: MdmDetectionResult? = null
)
