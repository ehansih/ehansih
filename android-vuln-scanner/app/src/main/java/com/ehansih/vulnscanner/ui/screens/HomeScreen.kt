package com.ehansih.vulnscanner.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.Severity
import com.ehansih.vulnscanner.data.models.ScanSummary
import com.ehansih.vulnscanner.ui.components.ScoreGauge
import com.ehansih.vulnscanner.ui.components.SeverityBadge
import com.ehansih.vulnscanner.ui.components.severityColor
import com.ehansih.vulnscanner.ui.theme.*
import com.ehansih.vulnscanner.viewmodel.ScanUiState

@Composable
fun HomeScreen(
    uiState: ScanUiState,
    onStartScan: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientMid, GradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = ColorGold,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "VulnScanner",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorGold
                    )
                    Text(
                        "Cyber Defence Suite",
                        color = ColorSilver,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Scan button / progress
            if (uiState.isScanning) {
                ScanningCard(message = uiState.message, progress = uiState.progress)
            } else {
                ShimmerScanButton(onClick = onStartScan)
            }

            Spacer(Modifier.height(24.dp))

            // Results summary (when available)
            uiState.summary?.let { summary ->
                if (!summary.cveNetworkAvailable) {
                    CveNetworkWarning()
                    Spacer(Modifier.height(12.dp))
                }
                GradientOverallScoreCard(summary)
                Spacer(Modifier.height(16.dp))
                SummaryStatsRow(summary)
                Spacer(Modifier.height(16.dp))
                NavigationCards(summary, onNavigate)
                Spacer(Modifier.height(16.dp))
                QuickActionsRow(onNavigate)
            } ?: run {
                if (!uiState.isScanning) {
                    PlaceholderCard()
                }
            }

            uiState.error?.let {
                Spacer(Modifier.height(16.dp))
                ErrorCard(it)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShimmerScanButton(onClick: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF64FFDA),
            Color(0xFF64FFDA).copy(alpha = 0.3f),
            Color(0xFFFFD700),
            Color(0xFF64FFDA).copy(alpha = 0.3f),
            Color(0xFF64FFDA)
        ),
        start = Offset(shimmerOffset * 600f, 0f),
        end = Offset(shimmerOffset * 600f + 400f, 100f)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(shimmerBrush),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(10.dp))
            Text(
                "START FULL SCAN",
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontSize = 17.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun ScanningCard(message: String, progress: Float) {
    val anim = rememberInfiniteTransition(label = "pulse")
    val alpha by anim.animateFloat(
        initialValue = 0.5f, targetValue = 1f, label = "alpha",
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse)
    )
    val scale by anim.animateFloat(
        initialValue = 0.97f, targetValue = 1.03f, label = "scale",
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse)
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorNavy.copy(alpha = 0.85f)),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = ColorGold,
                    strokeWidth = 2.5.dp
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    message,
                    color = ColorOnCard.copy(alpha = alpha),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(14.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = ColorGold,
                trackColor = Color.DarkGray
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "${(progress * 100).toInt()}%",
                color = ColorGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GradientOverallScoreCard(summary: ScanSummary) {
    val severityColor = severityColor(summary.overallRisk)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(ColorNavy, severityColor.copy(alpha = 0.55f)),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .border(1.dp, severityColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
    ) {
        Row(
            Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreGauge(score = summary.deviceScore, modifier = Modifier.weight(1f))
            Divider(
                modifier = Modifier
                    .height(80.dp)
                    .width(1.dp),
                color = Color.White.copy(alpha = 0.15f)
            )
            Column(Modifier.weight(2f).padding(start = 20.dp)) {
                Text("Overall Risk", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                SeverityBadge(summary.overallRisk)
                Spacer(Modifier.height(8.dp))
                Text("Last scanned", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                Text(
                    java.text.SimpleDateFormat(
                        "dd MMM yyyy HH:mm",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date(summary.scanTime)),
                    color = ColorOnCard,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SummaryStatsRow(summary: ScanSummary) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatChip(Modifier.weight(1f), "${summary.criticalCves}", "CRITICAL", ColorCritical)
        StatChip(Modifier.weight(1f), "${summary.highCves}", "HIGH CVEs", ColorOrange)
        StatChip(Modifier.weight(1f), "${summary.vulnerableApps}", "RISKY APPS", ColorTeal)
    }
}

@Composable
private fun StatChip(modifier: Modifier, value: String, label: String, color: Color) {
    Card(
        modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(listOf(color.copy(alpha = 0.5f), color.copy(alpha = 0.1f)))
        )
    ) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, color = color.copy(alpha = 0.75f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun NavigationCards(summary: ScanSummary, onNavigate: (String) -> Unit) {
    val deviceSev = when {
        summary.deviceResult.securityScore < 40 -> Severity.CRITICAL
        summary.deviceResult.securityScore < 60 -> Severity.HIGH
        summary.deviceResult.securityScore < 80 -> Severity.MEDIUM
        else -> Severity.LOW
    }
    val appSev = when {
        summary.criticalCves > 0 -> Severity.CRITICAL
        summary.highCves > 0 -> Severity.HIGH
        summary.vulnerableApps > 0 -> Severity.MEDIUM
        else -> Severity.LOW
    }
    val netSev = summary.networkResult?.findings
        ?.maxByOrNull { it.severity.ordinal }?.severity ?: Severity.LOW
    val attackSev = summary.attackIntelligence?.let {
        if (it.criticalAttackCount > 0) Severity.CRITICAL
        else if (it.relevantAttacks.isNotEmpty()) Severity.HIGH
        else Severity.LOW
    } ?: Severity.INFO

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        NavCard(
            icon = Icons.Default.PhoneAndroid,
            accentColor = ColorBlue,
            title = "Device Security",
            subtitle = "${summary.deviceResult.findings.size} findings",
            severity = deviceSev
        ) { onNavigate("device") }
        NavCard(
            icon = Icons.Default.Apps,
            accentColor = ColorPurple,
            title = "Installed Apps",
            subtitle = "${summary.totalApps} apps scanned · ${summary.vulnerableApps} at risk",
            severity = appSev
        ) { onNavigate("apps") }
        NavCard(
            icon = Icons.Default.Wifi,
            accentColor = ColorTeal,
            title = "Network Security",
            subtitle = summary.networkResult?.ssid ?: "No WiFi data",
            severity = netSev
        ) { onNavigate("network") }
        NavCard(
            icon = Icons.Default.BugReport,
            accentColor = ColorCritical,
            title = "Threats",
            subtitle = summary.attackIntelligence?.riskSummary ?: "No threats found",
            severity = attackSev
        ) { onNavigate("attack_intel") }
        NavCard(
            icon = Icons.Default.WarningAmber,
            accentColor = ColorOrange,
            title = "Malware Behavior",
            subtitle = "Accessibility, Device Admin, Overlay checks",
            severity = Severity.INFO
        ) { onNavigate("malware_behavior") }
        NavCard(
            icon = Icons.Default.Visibility,
            accentColor = ColorGold,
            title = "Permission Usage",
            subtitle = "Last 7 days camera, mic, location access",
            severity = Severity.INFO
        ) { onNavigate("permission_usage") }
        NavCard(
            icon = Icons.Default.Security,
            accentColor = ColorSilver,
            title = "Breach Monitor",
            subtitle = "Email breach & VirusTotal app scan",
            severity = Severity.INFO
        ) { onNavigate("breach_monitor") }
        NavCard(
            icon = Icons.Default.AdminPanelSettings,
            accentColor = ColorMedium,
            title = "MDM Detection",
            subtitle = "Corporate device management check",
            severity = Severity.INFO
        ) { onNavigate("mdm_detection") }
        NavCard(
            icon = Icons.Default.SimCard,
            accentColor = ColorInfo,
            title = "SIM Security",
            subtitle = "Network type & IMSI catcher risk",
            severity = Severity.INFO
        ) { onNavigate("sim_security") }
        NavCard(
            icon = Icons.Default.Bluetooth,
            accentColor = ColorBlue,
            title = "Bluetooth",
            subtitle = "Paired devices & discoverable status",
            severity = Severity.INFO
        ) { onNavigate("bluetooth") }
    }
}

@Composable
private fun NavCard(
    icon: ImageVector,
    accentColor: Color,
    title: String,
    subtitle: String,
    severity: Severity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Left border accent stripe
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(accentColor, accentColor.copy(alpha = 0.3f))
                        ),
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
            )
            Row(
                Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, color = ColorOnCard, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text(subtitle, color = Color.Gray, fontSize = 12.sp)
                }
                SeverityBadge(severity)
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Composable
private fun QuickActionsRow(onNavigate: (String) -> Unit) {
    Text(
        "Quick Actions",
        color = ColorSilver,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedButton(
            onClick = { /* Export PDF — not yet wired */ },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorTeal),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(listOf(ColorTeal, ColorBlue))
            )
        ) {
            Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Export PDF", fontSize = 13.sp)
        }
        OutlinedButton(
            onClick = { onNavigate("settings") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorSilver),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(listOf(ColorSilver, Color.Gray))
            )
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Settings", fontSize = 13.sp)
        }
    }
}

@Composable
private fun PlaceholderCard() {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.SecurityUpdateGood,
                null,
                tint = ColorSilver.copy(alpha = 0.5f),
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.height(14.dp))
            Text("No scan data yet", color = ColorSilver.copy(alpha = 0.8f), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Tap START FULL SCAN to analyse your device",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun CveNetworkWarning() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorMedium.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(listOf(ColorMedium.copy(alpha = 0.4f), ColorMedium.copy(alpha = 0.1f)))
        )
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CloudOff, null, tint = ColorMedium, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    "CVE Lookup Unavailable",
                    color = ColorMedium,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "NVD API was unreachable (DNS blocked — likely corporate/MDM network). " +
                        "Risk scores reflect permissions and install source only. " +
                        "Rescan on a personal network to fetch live CVE data.",
                    color = ColorMedium.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCritical.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(16.dp)) {
            Icon(Icons.Default.Error, null, tint = ColorCritical)
            Spacer(Modifier.width(10.dp))
            Text(message, color = ColorCritical, fontSize = 13.sp)
        }
    }
}
