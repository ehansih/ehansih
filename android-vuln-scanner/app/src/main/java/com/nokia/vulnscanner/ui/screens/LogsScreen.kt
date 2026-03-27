package com.nokia.vulnscanner.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nokia.vulnscanner.data.models.AppLogger
import com.nokia.vulnscanner.data.models.LogEntry
import com.nokia.vulnscanner.data.models.LogLevel
import com.nokia.vulnscanner.ui.theme.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LogsScreen() {
    val context = LocalContext.current
    val entries by AppLogger.entries.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var autoScroll by remember { mutableStateOf(true) }
    var filterLevel by remember { mutableStateOf<LogLevel?>(null) }

    val filtered = if (filterLevel == null) entries
                   else entries.filter { it.level == filterLevel }

    // Auto-scroll to bottom on new entries
    LaunchedEffect(filtered.size) {
        if (autoScroll && filtered.isNotEmpty()) {
            listState.animateScrollToItem(filtered.lastIndex)
        }
    }

    Column(Modifier.fillMaxSize().background(ColorSurface)) {

        // ── Top bar ────────────────────────────────────────────────────────────
        Row(
            Modifier.fillMaxWidth().background(ColorCard).padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Debug Logs", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                color = ColorOnCard, modifier = Modifier.weight(1f))

            // Filter chips
            LogLevel.entries.forEach { level ->
                val active = filterLevel == level
                FilterChip(
                    selected = active,
                    onClick  = { filterLevel = if (active) null else level },
                    label    = { Text(level.name.first().toString(),
                        fontSize = 10.sp, color = if (active) Color.Black else logColor(level)) },
                    modifier = Modifier.padding(horizontal = 2.dp).height(28.dp),
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = logColor(level),
                        containerColor         = logColor(level).copy(alpha = 0.15f)
                    )
                )
            }

            IconButton(onClick = { autoScroll = !autoScroll }) {
                Icon(if (autoScroll) Icons.Default.VerticalAlignBottom else Icons.Default.PauseCircle,
                    null, tint = if (autoScroll) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { copyToClipboard(context, AppLogger.allText()) }) {
                Icon(Icons.Default.ContentCopy, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { shareLog(context, AppLogger.allText()) }) {
                Icon(Icons.Default.Share, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { AppLogger.clear() }) {
                Icon(Icons.Default.DeleteSweep, null, tint = ColorCritical, modifier = Modifier.size(20.dp))
            }
        }

        // ── Entry count bar ────────────────────────────────────────────────────
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
            Text("${filtered.size} entries", color = Color.Gray, fontSize = 11.sp)
            if (filterLevel != null) {
                Text(" · filtered by ${filterLevel!!.name}", color = logColor(filterLevel!!), fontSize = 11.sp)
            }
        }

        // ── Log list ───────────────────────────────────────────────────────────
        if (filtered.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Terminal, null, tint = Color.DarkGray,
                        modifier = Modifier.size(40.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("No logs yet", color = Color.DarkGray, fontSize = 14.sp)
                    Text("Start a scan to collect logs", color = Color.DarkGray, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(state = listState,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items(filtered, key = { it.timestamp.toString() + it.message.hashCode() }) { entry ->
                    LogRow(entry)
                }
            }
        }
    }
}

@Composable
private fun LogRow(entry: LogEntry) {
    val color = logColor(entry.level)
    Row(
        Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        // Level indicator
        Text(entry.level.name.first().toString(),
            color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(12.dp))
        Spacer(Modifier.width(6.dp))
        Column(Modifier.horizontalScroll(rememberScrollState())) {
            Row {
                val time = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())
                    .format(java.util.Date(entry.timestamp))
                Text(time, color = Color.DarkGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Spacer(Modifier.width(6.dp))
                Text("[${entry.tag}]", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
            Text(entry.message, color = color.copy(alpha = 0.9f),
                fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            entry.throwable?.let {
                Text(it, color = ColorCritical.copy(alpha = 0.7f),
                    fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

private fun logColor(level: LogLevel): Color = when (level) {
    LogLevel.ERROR -> ColorCritical
    LogLevel.WARN  -> ColorHigh
    LogLevel.INFO  -> ColorInfo
    LogLevel.DEBUG -> Color(0xFF9E9E9E)
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("VulnScanner Logs", text))
}

private fun shareLog(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "VulnScanner Debug Logs")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Logs"))
}
