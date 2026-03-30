package com.ehansih.vulnscanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.BreachRecord
import com.ehansih.vulnscanner.data.models.VirusTotalResult
import com.ehansih.vulnscanner.ui.theme.*

@Composable
fun BreachMonitorScreen(
    vtResults: List<VirusTotalResult>,
    breachResults: List<BreachRecord>,
    isScanning: Boolean,
    onCheckEmail: (String) -> Unit,
    onScanApps: () -> Unit,
    hasVtApiKey: Boolean,
    hasHibpApiKey: Boolean
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
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(ColorCritical.copy(alpha = 0.25f), ColorOrange.copy(alpha = 0.25f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = ColorOrange,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Breach Monitor",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorOnCard
                )
                Text("Email breaches & app hash scanning", color = Color.Gray, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Section 1: Email Breach Check ────────────────────────────────────
        SectionHeader(
            icon = Icons.Default.Email,
            title = "Email Breach Check",
            color = ColorBlue,
            subtitle = "Powered by HaveIBeenPwned"
        )

        Spacer(Modifier.height(12.dp))

        if (!hasHibpApiKey) {
            ApiKeyMissingCard(
                icon = Icons.Default.Email,
                color = ColorBlue,
                message = "Configure HIBP API key in Settings to enable email breach checking."
            )
        } else {
            EmailCheckCard(
                breachResults = breachResults,
                onCheckEmail = onCheckEmail
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── Section 2: VirusTotal App Hash Scanner ────────────────────────────
        SectionHeader(
            icon = Icons.Default.BugReport,
            title = "App Hash Scanner",
            color = ColorOrange,
            subtitle = "Powered by VirusTotal"
        )

        Spacer(Modifier.height(12.dp))

        if (!hasVtApiKey) {
            ApiKeyMissingCard(
                icon = Icons.Default.BugReport,
                color = ColorOrange,
                message = "Configure VirusTotal API key in Settings to enable app hash scanning."
            )
        } else {
            VirusTotalSection(
                results = vtResults,
                isScanning = isScanning,
                onScanApps = onScanApps
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: Color,
    subtitle: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.Gray, fontSize = 11.sp)
        }
    }
    Divider(color = color.copy(alpha = 0.2f), modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun ApiKeyMissingCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Key, null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(12.dp))
            Text(message, color = color.copy(alpha = 0.9f), fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun EmailCheckCard(
    breachResults: List<BreachRecord>,
    onCheckEmail: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email address") },
                leadingIcon = {
                    Icon(Icons.Default.Email, null, tint = ColorBlue)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    if (email.isNotBlank()) onCheckEmail(email.trim())
                }),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorBlue,
                    focusedLabelColor = ColorBlue,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = ColorOnCard,
                    unfocusedTextColor = ColorOnCard
                ),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { if (email.isNotBlank()) onCheckEmail(email.trim()) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ColorBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Search, null)
                Spacer(Modifier.width(8.dp))
                Text("CHECK BREACHES", fontWeight = FontWeight.Bold)
            }

            if (breachResults.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "${breachResults.size} breach${if (breachResults.size > 1) "es" else ""} found",
                    color = ColorCritical,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    breachResults.forEach { breach ->
                        BreachRecordCard(breach)
                    }
                }
            } else if (email.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = ColorLow, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("No known breaches for this email.", color = ColorLow, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun BreachRecordCard(breach: BreachRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCritical.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(breach.name, color = ColorOnCard, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (breach.isVerified) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ColorCritical.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("VERIFIED", color = ColorCritical, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(breach.breachDate, color = Color.Gray, fontSize = 11.sp)
            }
            if (breach.description.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(breach.description, color = Color.LightGray, fontSize = 12.sp, lineHeight = 17.sp)
            }
            if (breach.dataClasses.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text("Data exposed:", color = ColorOrange, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    breach.dataClasses.take(4).forEach { dataType ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ColorOrange.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(dataType, color = ColorOrange, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    if (breach.dataClasses.size > 4) {
                        Text("+${breach.dataClasses.size - 4} more", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun VirusTotalSection(
    results: List<VirusTotalResult>,
    isScanning: Boolean,
    onScanApps: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Button(
                onClick = onScanApps,
                enabled = !isScanning,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ColorOrange),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Scanning apps...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.BugReport, null)
                    Spacer(Modifier.width(8.dp))
                    Text("SCAN ALL APPS", fontWeight = FontWeight.Bold)
                }
            }

            if (isScanning) {
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = ColorOrange,
                    trackColor = Color.DarkGray
                )
            }

            val flagged = results.filter { it.isFlagged }
            if (flagged.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "${flagged.size} flagged app${if (flagged.size > 1) "s" else ""}",
                    color = ColorCritical,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    flagged.forEach { vt ->
                        VtResultCard(vt)
                    }
                }
            } else if (results.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = ColorLow, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("All scanned apps are clean.", color = ColorLow, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun VtResultCard(result: VirusTotalResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCritical.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(result.appName, color = ColorOnCard, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ColorCritical.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        "${result.maliciousCount}/${result.totalEngines}",
                        color = ColorCritical,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(3.dp))
            Text(result.packageName, color = Color.Gray, fontSize = 11.sp)
            if (result.detectionNames.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Detections: ${result.detectionNames.take(3).joinToString(", ")}${if (result.detectionNames.size > 3) "..." else ""}",
                    color = ColorOrange,
                    fontSize = 11.sp
                )
            }
        }
    }
}
