package com.ehansih.vulnscanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.DeviceSecurityResult
import com.ehansih.vulnscanner.data.models.SecurityFinding
import com.ehansih.vulnscanner.data.models.Severity
import com.ehansih.vulnscanner.ui.components.ScoreGauge
import com.ehansih.vulnscanner.ui.components.SeverityBadge
import com.ehansih.vulnscanner.ui.components.severityColor
import com.ehansih.vulnscanner.ui.theme.*

@Composable
fun DeviceScreen(result: DeviceSecurityResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(ColorSurface),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Device Security", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                color = ColorOnCard, modifier = Modifier.padding(bottom = 4.dp))
        }

        // Score card
        item {
            Card(Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCard),
                shape  = RoundedCornerShape(16.dp)) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    ScoreGauge(result.securityScore)
                    Spacer(Modifier.width(20.dp))
                    Column {
                        InfoRow("Android", result.androidVersion)
                        InfoRow("Patch Level", result.securityPatchLevel)
                    }
                }
            }
        }

        // Security checklist
        item {
            Text("Security Checklist", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                color = ColorOnCard, modifier = Modifier.padding(top = 4.dp))
        }
        item {
            Card(Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCard),
                shape  = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CheckItem("Screen Lock",       result.isScreenLockEnabled,  Icons.Default.Lock)
                    CheckItem("Storage Encrypted", result.isDeviceEncrypted,    Icons.Default.Security)
                    CheckItem("Biometric Enabled", result.isBiometricEnabled,   Icons.Default.Fingerprint)
                    CheckItem("Play Protect On",   result.isVerifyAppsEnabled,  Icons.Default.Shield)
                    CheckItem("Not Rooted",        !result.isRooted,            Icons.Default.AdminPanelSettings)
                    CheckItem("Dev Mode Off",      !result.isDeveloperModeEnabled, Icons.Default.DeveloperMode)
                    CheckItem("USB Debug Off",     !result.isUsbDebuggingEnabled,  Icons.Default.Usb)
                    CheckItem("Unknown Sources Off", !result.isUnknownSourcesAllowed, Icons.Default.InstallMobile)
                }
            }
        }

        // Findings
        if (result.findings.isNotEmpty()) {
            item {
                Text("Findings & Recommendations", fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold, color = ColorOnCard,
                    modifier = Modifier.padding(top = 4.dp))
            }
            items(result.findings.sortedBy { it.severity.ordinal }) { finding ->
                FindingCard(finding)
            }
        }

        // OS CVEs
        if (result.osVulnerabilities.isNotEmpty()) {
            item {
                Text("OS Vulnerabilities (${result.osVulnerabilities.size})",
                    fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = ColorOnCard,
                    modifier = Modifier.padding(top = 4.dp))
            }
            items(result.osVulnerabilities.sortedByDescending { it.cvssScore }) { cve ->
                CveCard(cveId = cve.cveId, description = cve.description,
                    cvss = cve.cvssScore, severity = cve.severity)
            }
        }
    }
}

@Composable
private fun CheckItem(label: String, ok: Boolean, icon: ImageVector) {
    val color = if (ok) ColorOk else ColorCritical
    val statusText = if (ok) "OK" else "FAIL"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, color = ColorOnCard, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Box(Modifier.clip(RoundedCornerShape(4.dp)).background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 2.dp)) {
            Text(statusText, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FindingCard(finding: SecurityFinding) {
    val color = severityColor(finding.severity)
    var expanded by remember { mutableStateOf(false) }
    Card(
        onClick   = { expanded = !expanded },
        modifier  = Modifier.fillMaxWidth(),
        colors    = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        shape     = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, null, tint = color, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(finding.title, color = ColorOnCard, fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp, modifier = Modifier.weight(1f))
                SeverityBadge(finding.severity)
            }
            if (expanded) {
                Spacer(Modifier.height(10.dp))
                Text("What's wrong:", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(finding.detail, color = ColorOnCard.copy(alpha = 0.85f), fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp))
                HorizontalDivider(color = Color.DarkGray)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.CheckCircle, null, tint = ColorOk, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Column {
                        Text("How to fix:", color = ColorOk, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(finding.recommendation, color = ColorOnCard.copy(alpha = 0.85f), fontSize = 13.sp,
                            modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CveCard(cveId: String, description: String, cvss: Double, severity: Severity) {
    var expanded by remember { mutableStateOf(false) }
    val color = severityColor(severity)
    Card(
        onClick  = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(cveId, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp,
                    modifier = Modifier.weight(1f))
                Text("CVSS $cvss", color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(8.dp))
                SeverityBadge(severity)
            }
            if (expanded) {
                Spacer(Modifier.height(6.dp))
                Text(description, color = ColorOnCard.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row {
        Text("$label: ", color = Color.Gray, fontSize = 13.sp)
        Text(value, color = ColorOnCard, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
