package com.nokia.vulnscanner.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nokia.vulnscanner.data.models.Severity
import com.nokia.vulnscanner.data.models.ScanSummary
import com.nokia.vulnscanner.ui.components.ScoreGauge
import com.nokia.vulnscanner.ui.components.SeverityBadge
import com.nokia.vulnscanner.ui.components.severityColor
import com.nokia.vulnscanner.ui.theme.*
import com.nokia.vulnscanner.viewmodel.ScanUiState

@Composable
fun HomeScreen(
    uiState: ScanUiState,
    onStartScan: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Shield, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(10.dp))
            Text("VulnScanner", fontSize = 24.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary)
        }
        Text("Nokia Networks · Device Vulnerability Scanner",
            color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(start = 42.dp))

        Spacer(Modifier.height(24.dp))

        // Scan button / progress
        if (uiState.isScanning) {
            ScanningCard(message = uiState.message, progress = uiState.progress)
        } else {
            Button(
                onClick  = onStartScan,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape    = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text("START FULL SCAN", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Results summary (when available)
        uiState.summary?.let { summary ->
            OverallScoreCard(summary)
            Spacer(Modifier.height(16.dp))
            SummaryStatsRow(summary)
            Spacer(Modifier.height(16.dp))
            NavigationCards(summary, onNavigate)
        } ?: run {
            if (!uiState.isScanning) {
                PlaceholderCard()
            }
        }

        uiState.error?.let {
            Spacer(Modifier.height(16.dp))
            ErrorCard(it)
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color    = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(12.dp))
                Text(message, color = ColorOnCard.copy(alpha = alpha), fontSize = 14.sp)
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color    = MaterialTheme.colorScheme.primary,
                trackColor = Color.DarkGray
            )
            Text("${(progress * 100).toInt()}%", color = Color.Gray, fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun OverallScoreCard(summary: ScanSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(16.dp)
    ) {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            ScoreGauge(score = summary.deviceScore, modifier = Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.height(80.dp).width(1.dp), color = Color.DarkGray)
            Column(Modifier.weight(2f).padding(start = 20.dp)) {
                Text("Overall Risk", color = Color.Gray, fontSize = 12.sp)
                SeverityBadge(summary.overallRisk)
                Spacer(Modifier.height(8.dp))
                Text("Last scanned", color = Color.Gray, fontSize = 11.sp)
                Text(
                    java.text.SimpleDateFormat("dd MMM yyyy HH:mm", java.util.Locale.getDefault())
                        .format(java.util.Date(summary.scanTime)),
                    color = ColorOnCard, fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SummaryStatsRow(summary: ScanSummary) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatChip(Modifier.weight(1f), "${summary.criticalCves}", "CRITICAL CVEs", ColorCritical)
        StatChip(Modifier.weight(1f), "${summary.highCves}", "HIGH CVEs", ColorHigh)
        StatChip(Modifier.weight(1f), "${summary.vulnerableApps}", "RISKY APPS", ColorMedium)
    }
}

@Composable
private fun StatChip(modifier: Modifier, value: String, label: String, color: Color) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, color = color.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun NavigationCards(summary: ScanSummary, onNavigate: (String) -> Unit) {
    val deviceSev = when {
        summary.deviceResult.securityScore < 40 -> Severity.CRITICAL
        summary.deviceResult.securityScore < 60 -> Severity.HIGH
        summary.deviceResult.securityScore < 80 -> Severity.MEDIUM
        else                                     -> Severity.LOW
    }
    val appSev = when {
        summary.criticalCves > 0  -> Severity.CRITICAL
        summary.highCves > 0      -> Severity.HIGH
        summary.vulnerableApps > 0 -> Severity.MEDIUM
        else                       -> Severity.LOW
    }
    val netSev = summary.networkResult?.findings?.maxByOrNull { it.severity.ordinal }?.severity ?: Severity.LOW

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        NavCard(Icons.Default.PhoneAndroid, "Device Security",
            "${summary.deviceResult.findings.size} findings", deviceSev) { onNavigate("device") }
        NavCard(Icons.Default.Apps, "Installed Apps",
            "${summary.totalApps} apps scanned · ${summary.vulnerableApps} at risk", appSev) { onNavigate("apps") }
        NavCard(Icons.Default.Wifi, "Network Security",
            summary.networkResult?.ssid ?: "No WiFi data", netSev) { onNavigate("network") }
    }
}

@Composable
private fun NavCard(icon: ImageVector, title: String, subtitle: String,
                    severity: Severity, onClick: () -> Unit) {
    val color = severityColor(severity)
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
                .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = ColorOnCard, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            SeverityBadge(severity)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
private fun PlaceholderCard() {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.SecurityUpdateGood, null,
                tint = Color.Gray, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(12.dp))
            Text("No scan data yet", color = Color.Gray, fontSize = 14.sp)
            Text("Tap START FULL SCAN to check your device",
                color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
        containerColor = ColorCritical.copy(alpha = 0.15f)), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(16.dp)) {
            Icon(Icons.Default.Error, null, tint = ColorCritical)
            Spacer(Modifier.width(10.dp))
            Text(message, color = ColorCritical, fontSize = 13.sp)
        }
    }
}
