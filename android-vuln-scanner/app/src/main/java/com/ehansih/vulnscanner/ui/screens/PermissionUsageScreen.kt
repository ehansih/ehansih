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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.PermissionUsageEntry
import com.ehansih.vulnscanner.data.models.PermissionUsageResult
import com.ehansih.vulnscanner.ui.theme.*

private data class PermGroup(
    val label: String,
    val icon: ImageVector,
    val color: Color
)

private val permissionGroups = mapOf(
    "Camera"    to PermGroup("Camera",    Icons.Default.CameraAlt,    ColorTeal),
    "Microphone" to PermGroup("Microphone", Icons.Default.Mic,          ColorCritical),
    "Location"  to PermGroup("Location",  Icons.Default.LocationOn,   ColorBlue),
    "SMS"       to PermGroup("SMS",       Icons.Default.Sms,           ColorPurple),
    "Contacts"  to PermGroup("Contacts",  Icons.Default.Contacts,      ColorGold)
)

private fun humanReadableTime(nowMs: Long, lastMs: Long): String {
    val diffMs = nowMs - lastMs
    val minutes = diffMs / 60_000
    val hours = diffMs / 3_600_000
    val days = diffMs / 86_400_000
    return when {
        minutes < 1  -> "Just now"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24   -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days == 1L   -> "Yesterday"
        days < 7     -> "$days days ago"
        else         -> "Over a week ago"
    }
}

@Composable
fun PermissionUsageScreen(result: PermissionUsageResult?) {
    val now = System.currentTimeMillis()

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
                    .background(ColorBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = null,
                    tint = ColorBlue,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Permission Usage",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorOnCard
                )
                Text("Last 7 Days", color = Color.Gray, fontSize = 12.sp)
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
                    Icon(Icons.Default.Visibility, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Run a scan to see permission usage.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            return@Column
        }

        val backgroundAccess = result.entries.filter { it.isBackgroundAccess }
        if (backgroundAccess.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCritical.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        null,
                        tint = ColorCritical,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            "Background Camera/Microphone Access Detected",
                            color = ColorCritical,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${backgroundAccess.size} app(s) accessed sensitive sensors in the background — potential spyware indicator.",
                            color = ColorCritical.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        if (result.entries.isEmpty()) {
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, null, tint = ColorLow, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("No recent permission access recorded.", color = ColorLow, fontSize = 14.sp)
                }
            }
            return@Column
        }

        val grouped = result.entries.groupBy { it.permission }

        permissionGroups.forEach { (key, group) ->
            val entries = grouped[key]
            if (!entries.isNullOrEmpty()) {
                PermissionGroupSection(group = group, entries = entries, now = now)
                Spacer(Modifier.height(16.dp))
            }
        }

        // Any uncategorised
        val knownKeys = permissionGroups.keys
        val other = grouped.filterKeys { it !in knownKeys }
        other.forEach { (key, entries) ->
            val group = PermGroup(key, Icons.Default.Lock, ColorSilver)
            PermissionGroupSection(group = group, entries = entries, now = now)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PermissionGroupSection(
    group: PermGroup,
    entries: List<PermissionUsageEntry>,
    now: Long
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(group.icon, contentDescription = null, tint = group.color, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(group.label, color = group.color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(group.color.copy(alpha = 0.18f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("${entries.size}", color = group.color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            entries.forEach { entry ->
                PermissionEntryCard(entry = entry, accentColor = group.color, now = now)
            }
        }
    }
}

@Composable
private fun PermissionEntryCard(
    entry: PermissionUsageEntry,
    accentColor: Color,
    now: Long
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isBackgroundAccess)
                ColorCritical.copy(alpha = 0.08f)
            else
                ColorCard
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Android,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(entry.appName, color = ColorOnCard, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(entry.packageName, color = Color.Gray, fontSize = 11.sp)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        humanReadableTime(now, entry.lastAccessed),
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
            if (entry.isBackgroundAccess) {
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ColorCritical.copy(alpha = 0.25f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "BACKGROUND",
                        color = ColorCritical,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
