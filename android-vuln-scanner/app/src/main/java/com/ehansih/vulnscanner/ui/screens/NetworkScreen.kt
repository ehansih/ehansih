package com.ehansih.vulnscanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.NetworkSecurityResult
import com.ehansih.vulnscanner.ui.theme.*

@Composable
fun NetworkScreen(result: NetworkSecurityResult?) {
    if (result == null) {
        Box(Modifier.fillMaxSize().background(ColorSurface), contentAlignment = Alignment.Center) {
            Text("No network data available", color = Color.Gray)
        }
        return
    }

    LazyColumn(
        Modifier.fillMaxSize().background(ColorSurface),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Network Security", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ColorOnCard) }

        item {
            Card(Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCard),
                shape  = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    NetworkRow(Icons.Default.Wifi,       "SSID",          result.ssid)
                    NetworkRow(Icons.Default.Router,     "Security Type", result.securityType)
                    NetworkRow(Icons.Default.Https,      "TLS Version",   result.tlsVersion)
                    NetworkRow(Icons.Default.VpnKey,     "VPN Active",
                        if (result.isVpnActive) "Yes ✓" else "No ✗",
                        if (result.isVpnActive) ColorOk else ColorHigh)
                    NetworkRow(Icons.Default.SettingsEthernet, "Proxy Set",
                        if (result.isProxySet) "Yes – verify manually" else "No",
                        if (result.isProxySet) ColorMedium else ColorOk)
                }
            }
        }

        if (result.findings.isNotEmpty()) {
            item {
                Text("Findings & Recommendations", fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold, color = ColorOnCard)
            }
            items(result.findings.sortedBy { it.severity.ordinal }) { finding ->
                FindingCard(finding)
            }
        } else {
            item {
                Card(Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ColorOk.copy(0.1f)),
                    shape  = RoundedCornerShape(12.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = ColorOk)
                        Spacer(Modifier.width(10.dp))
                        Text("Network looks good!", color = ColorOk, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun NetworkRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: Color = ColorOnCard
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text("$label:", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.width(110.dp))
        Text(value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
