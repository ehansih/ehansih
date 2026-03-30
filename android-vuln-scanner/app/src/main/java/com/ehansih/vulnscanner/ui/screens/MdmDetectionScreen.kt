package com.ehansih.vulnscanner.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.DeviceAdminEntry
import com.ehansih.vulnscanner.data.models.MdmDetectionResult
import com.ehansih.vulnscanner.ui.theme.*

private val knownMdmVendors = mapOf(
    "com.airwatch"           to Pair("AirWatch / Workspace ONE", ColorBlue),
    "com.mobileiron"         to Pair("MobileIron", ColorOrange),
    "com.meraki"             to Pair("Cisco Meraki", ColorTeal),
    "com.jamf"               to Pair("Jamf", ColorPurple),
    "com.microsoft.intune"   to Pair("Microsoft Intune", ColorBlue),
    "com.ibm.mas"            to Pair("IBM MaaS360", ColorNavy),
    "com.vmware.horizon"     to Pair("VMware Horizon", ColorBlue),
    "com.blackberry"         to Pair("BlackBerry UEM", ColorOk),
    "com.citrix"             to Pair("Citrix Endpoint", ColorTeal),
    "com.soti.mobicontrol"   to Pair("SOTI MobiControl", ColorGold)
)

private fun vendorInfo(packageName: String): Pair<String, Color>? =
    knownMdmVendors.entries.firstOrNull { packageName.contains(it.key) }?.value

@Composable
fun MdmDetectionScreen(result: MdmDetectionResult?) {
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
                    .background(ColorOrange.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = ColorOrange,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("MDM Detection", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ColorOnCard)
                Text("Corporate device management analysis", color = Color.Gray, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        if (result == null) {
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AdminPanelSettings, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Run a scan to detect MDM profiles.", color = Color.Gray, fontSize = 14.sp)
                }
            }
            return@Column
        }

        // Corporate device banner
        if (result.isManaged) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(ColorOrange.copy(alpha = 0.3f), ColorCritical.copy(alpha = 0.2f))
                        )
                    )
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Business,
                        null,
                        tint = ColorOrange,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "CORPORATE DEVICE DETECTED",
                            color = ColorOrange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            "This device is enrolled in a mobile device management system.",
                            color = ColorOrange.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        } else {
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorLow.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = ColorLow, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("No MDM enrollment detected.", color = ColorLow, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Device Owner
        result.deviceOwnerPackage?.let { owner ->
            SectionTitle(Icons.Default.Lock, "Device Owner", ColorCritical)
            Spacer(Modifier.height(8.dp))
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCritical.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AdminPanelSettings, null, tint = ColorCritical, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Device Owner Package", color = Color.Gray, fontSize = 11.sp)
                        Text(owner, color = ColorOnCard, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // Work Profile
        SectionTitle(Icons.Default.Work, "Work Profile", ColorTeal)
        Spacer(Modifier.height(8.dp))
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ColorCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (result.hasWorkProfile) Icons.Default.WorkspacePremium else Icons.Default.WorkOff,
                    null,
                    tint = if (result.hasWorkProfile) ColorTeal else Color.Gray,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    if (result.hasWorkProfile) "Work profile is active on this device"
                    else "No work profile detected",
                    color = if (result.hasWorkProfile) ColorTeal else Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Device Admins
        if (result.deviceAdmins.isNotEmpty()) {
            SectionTitle(Icons.Default.Shield, "Device Admin Apps", ColorOrange)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                result.deviceAdmins.forEach { admin ->
                    DeviceAdminCard(admin)
                }
            }
            Spacer(Modifier.height(20.dp))
        }

        // What this means — expandable
        WhatThisMeansSection()
    }
}

@Composable
private fun SectionTitle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeviceAdminCard(admin: DeviceAdminEntry) {
    val vendor = vendorInfo(admin.packageName)
    val accentColor = vendor?.second ?: ColorSilver
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(accentColor, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            )
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Shield, null, tint = accentColor, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(admin.appName, color = ColorOnCard, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(admin.packageName, color = Color.Gray, fontSize = 11.sp)
                    vendor?.first?.let { vendorName ->
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Business, null, tint = accentColor, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(vendorName, color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WhatThisMeansSection() {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = ColorInfo, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("What this means", color = ColorInfo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    null,
                    tint = Color.Gray
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBullet(
                        "Device Owner",
                        "An app with Device Owner privileges can silently install/uninstall apps, wipe the device, enforce policies, and monitor all activity including screenshots.",
                        ColorCritical
                    )
                    InfoBullet(
                        "Work Profile",
                        "A managed work profile (Android Enterprise) creates a separate container for corporate apps. IT can remotely wipe the work profile and may monitor work apps.",
                        ColorTeal
                    )
                    InfoBullet(
                        "Device Admin",
                        "Device Administrator apps can enforce password policies, lock the screen, and perform remote wipes — commonly used by MDM solutions.",
                        ColorOrange
                    )
                    InfoBullet(
                        "Your Privacy",
                        "If this is a personal device, an unexpected Device Owner or unknown MDM app could indicate stalkerware or unauthorised enterprise enrolment.",
                        ColorPurple
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoBullet(title: String, detail: String, color: Color) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(7.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(detail, color = Color.Gray, fontSize = 11.sp, lineHeight = 16.sp)
        }
    }
}
