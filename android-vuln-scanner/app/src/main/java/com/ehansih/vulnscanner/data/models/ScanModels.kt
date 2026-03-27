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
    val affectedProduct: String,   // keyword used to find this CVE
    val references: String         // comma-separated URLs
)

// ── App scan result ──────────────────────────────────────────────────────────

data class AppScanResult(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val versionCode: Long,
    val installedFrom: String,       // Play Store / Unknown / Sideloaded
    val permissions: List<PermissionEntry>,
    val cves: List<CveRecord>,
    val riskScore: Int,              // 0-100
    val flags: List<String>          // human-readable risk flags
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
    val securityScore: Int,          // 0-100
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
    val securityType: String,        // WEP / WPA / WPA2 / WPA3 / Open
    val isVpnActive: Boolean,
    val isProxySet: Boolean,
    val tlsVersion: String,
    val findings: List<SecurityFinding>
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
    val networkResult: NetworkSecurityResult?
)
