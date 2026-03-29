package com.ehansih.vulnscanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.ehansih.vulnscanner.data.models.*
import com.ehansih.vulnscanner.ui.components.SeverityBadge
import com.ehansih.vulnscanner.ui.components.severityColor
import com.ehansih.vulnscanner.ui.theme.*

@Composable
fun AttackIntelScreen(intel: AttackIntelligence?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.GppBad,
                contentDescription = null,
                tint = ColorCritical,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    "Attack Intelligence",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorOnCard
                )
                Text(
                    "Real-world attacks matching your scan",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        if (intel == null || intel.relevantAttacks.isEmpty()) {
            NoThreatCard()
            return@Column
        }

        // Risk summary banner
        RiskSummaryBanner(intel)
        Spacer(Modifier.height(16.dp))

        // Impact stats row
        ImpactStatsRow(intel)
        Spacer(Modifier.height(20.dp))

        // Attack list
        Text(
            "Matched Attack Campaigns  (${intel.relevantAttacks.size})",
            color = ColorOnCard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(10.dp))

        intel.relevantAttacks.forEach { attack ->
            AttackCard(attack)
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "Sources: CISA, NVD, Kaspersky Threat Intel, Lookout Security, Google TAG reports.",
            color = Color.DarkGray,
            fontSize = 10.sp
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun RiskSummaryBanner(intel: AttackIntelligence) {
    val bannerColor = if (intel.criticalAttackCount >= 1) ColorCritical else ColorHigh
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = bannerColor.copy(alpha = 0.15f)),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = bannerColor,
                modifier = Modifier.size(20.dp).padding(top = 2.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(intel.riskSummary, color = bannerColor, fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
}

@Composable
private fun ImpactStatsRow(intel: AttackIntelligence) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ImpactStat(
            modifier = Modifier.weight(1f),
            value    = intel.totalFinancialExposure,
            label    = "DOCUMENTED\nLOSSES",
            color    = ColorCritical
        )
        ImpactStat(
            modifier = Modifier.weight(1f),
            value    = "${intel.criticalAttackCount}",
            label    = "CRITICAL\nCAMPAIGNS",
            color    = ColorHigh
        )
        ImpactStat(
            modifier = Modifier.weight(1f),
            value    = "${intel.relevantAttacks.size}",
            label    = "TOTAL\nMATCHES",
            color    = ColorMedium
        )
    }
}

@Composable
private fun ImpactStat(modifier: Modifier, value: String, label: String, color: Color) {
    Card(
        modifier = modifier,
        colors   = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(label, color = color.copy(alpha = 0.7f), fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold, lineHeight = 13.sp)
        }
    }
}

@Composable
private fun AttackCard(attack: AttackIncident) {
    val severityColor = severityColor(attack.severity)
    val typeColor = when (attack.attackType) {
        AttackType.BANKING_TROJAN     -> Color(0xFFE53935)
        AttackType.SPYWARE            -> Color(0xFF8E24AA)
        AttackType.RANSOMWARE         -> Color(0xFFFF6F00)
        AttackType.DATA_THEFT         -> Color(0xFF1E88E5)
        AttackType.SUBSCRIPTION_FRAUD -> Color(0xFFFDD835)
        AttackType.REMOTE_ACCESS      -> Color(0xFFE53935)
        AttackType.STALKERWARE        -> Color(0xFF8E24AA)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Top row: name + severity badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        attack.name,
                        color = ColorOnCard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(attack.year, color = Color.Gray, fontSize = 11.sp)
                }
                SeverityBadge(attack.severity)
            }

            Spacer(Modifier.height(10.dp))

            // Type badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(typeColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    attack.attackType.label.uppercase(),
                    color = typeColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))
            Divider(color = Color.DarkGray, thickness = 0.5.dp)
            Spacer(Modifier.height(10.dp))

            // Impact stats
            ImpactRow(Icons.Default.MonetizationOn, "Financial Impact", attack.financialImpact, ColorCritical)
            Spacer(Modifier.height(6.dp))
            ImpactRow(Icons.Default.Storage, "Data Breach", attack.dataBreachImpact, ColorHigh)
            Spacer(Modifier.height(6.dp))
            ImpactRow(Icons.Default.Devices, "Scale", attack.affectedDevices, ColorMedium)

            Spacer(Modifier.height(10.dp))
            Divider(color = Color.DarkGray, thickness = 0.5.dp)
            Spacer(Modifier.height(10.dp))

            // Description
            Text(attack.description, color = Color.LightGray, fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun ImpactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = color,
            modifier = Modifier.size(14.dp).padding(top = 1.dp))
        Spacer(Modifier.width(6.dp))
        Text("$label: ", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text(value, color = ColorOnCard, fontSize = 12.sp)
    }
}

@Composable
private fun NoThreatCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.VerifiedUser, null,
                tint = ColorLow, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(12.dp))
            Text("No Threats Matched", color = ColorLow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Your current scan findings do not match any documented mobile attack campaigns in our database.",
                color = Color.Gray,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(8.dp))
            Text("Run a scan first to see attack intelligence.",
                color = Color.DarkGray, fontSize = 11.sp)
        }
    }
}
