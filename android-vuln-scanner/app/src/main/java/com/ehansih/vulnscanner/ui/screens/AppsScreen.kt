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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.AppScanResult
import com.ehansih.vulnscanner.data.models.Severity
import com.ehansih.vulnscanner.ui.components.SeverityBadge
import com.ehansih.vulnscanner.ui.components.severityColor
import com.ehansih.vulnscanner.ui.theme.*

@Composable
fun AppsScreen(apps: List<AppScanResult>) {
    var filterRisky by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val displayed = apps
        .filter { !filterRisky || it.riskScore > 0 }
        .filter { query.isBlank() || it.appName.contains(query, ignoreCase = true) }

    Column(Modifier.fillMaxSize().background(ColorSurface)) {
        // Top bar
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Installed Apps (${apps.size})", fontSize = 22.sp,
                fontWeight = FontWeight.Bold, color = ColorOnCard)

            OutlinedTextField(
                value = query, onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search apps…", color = Color.Gray) },
                leadingIcon  = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                singleLine   = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor     = ColorOnCard,
                    unfocusedTextColor   = ColorOnCard
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = filterRisky, onCheckedChange = { filterRisky = it },
                    colors = CheckboxDefaults.colors(checkedColor = ColorHigh))
                Text("Show risky only", color = ColorOnCard, fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Text("${displayed.size} shown", color = Color.Gray, fontSize = 12.sp)
            }
        }

        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(displayed, key = { it.packageName }) { app ->
                AppCard(app)
            }
        }
    }
}

@Composable
private fun AppCard(app: AppScanResult) {
    var expanded by remember { mutableStateOf(false) }

    val severity = when {
        app.cves.any { it.severity == Severity.CRITICAL } -> Severity.CRITICAL
        app.cves.any { it.severity == Severity.HIGH }     -> Severity.HIGH
        app.riskScore >= 30                               -> Severity.MEDIUM
        app.riskScore > 0                                 -> Severity.LOW
        else                                              -> Severity.INFO
    }
    val color = severityColor(severity)

    Card(
        onClick  = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            // App header row
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Risk score circle
                Box(
                    modifier = Modifier.size(42.dp).background(
                        color.copy(alpha = 0.15f), RoundedCornerShape(21.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${app.riskScore}", color = color,
                        fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(app.appName, color = ColorOnCard, fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp)
                    Text("v${app.versionName}", color = Color.Gray, fontSize = 11.sp)
                }
                if (severity != Severity.INFO) SeverityBadge(severity)
            }

            // Quick flags
            if (app.flags.isNotEmpty() && !expanded) {
                Spacer(Modifier.height(6.dp))
                Text(app.flags.first(), color = color.copy(alpha = 0.8f), fontSize = 11.sp)
            }

            // Expanded details
            if (expanded) {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color.DarkGray)
                Spacer(Modifier.height(10.dp))

                DetailRow("Package",   app.packageName)
                DetailRow("Version",   app.versionName)
                DetailRow("Source",    app.installedFrom)
                DetailRow("Dangerous permissions",
                    "${app.permissions.count { it.isDangerous && it.isGranted }} granted")

                if (app.flags.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text("Risk Flags", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    app.flags.forEach { flag ->
                        Row(Modifier.padding(top = 3.dp)) {
                            Text("•  ", color = ColorHigh, fontSize = 12.sp)
                            Text(flag, color = ColorOnCard.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }

                // Dangerous permissions list
                val dangerousGranted = app.permissions.filter { it.isDangerous && it.isGranted }
                if (dangerousGranted.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text("Dangerous Permissions Granted", color = Color.Gray,
                        fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    dangerousGranted.forEach { perm ->
                        Row(Modifier.padding(top = 3.dp)) {
                            Icon(Icons.Default.Warning, null, tint = ColorHigh,
                                modifier = Modifier.size(14.dp).padding(top = 2.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(perm.name.substringAfterLast('.'),
                                color = ColorOnCard.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }

                // CVEs
                if (app.cves.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text("CVEs (${app.cves.size})", color = Color.Gray,
                        fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    app.cves.take(5).forEach { cve ->
                        CveCard(cve.cveId, cve.description, cve.cvssScore, cve.severity)
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.padding(vertical = 2.dp)) {
        Text("$label:  ", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(value, color = ColorOnCard.copy(alpha = 0.85f), fontSize = 12.sp)
    }
}
