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
    val attackIntelligence: AttackIntelligence? = null
)
