package com.nokia.vulnscanner.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Severity colours ──────────────────────────────────────────────────────────
val ColorCritical = Color(0xFFE53935)   // red
val ColorHigh     = Color(0xFFFF6F00)   // deep orange/amber
val ColorMedium   = Color(0xFFFDD835)   // yellow
val ColorLow      = Color(0xFF43A047)   // green
val ColorInfo     = Color(0xFF1E88E5)   // blue
val ColorOk       = Color(0xFF2E7D32)   // dark green

val ColorSurface  = Color(0xFF121212)
val ColorCard     = Color(0xFF1E1E1E)
val ColorOnCard   = Color(0xFFEEEEEE)

private val DarkColorScheme = darkColorScheme(
    primary         = Color(0xFF64FFDA),
    onPrimary       = Color(0xFF000000),
    secondary       = Color(0xFF80CBC4),
    background      = ColorSurface,
    surface         = ColorCard,
    onSurface       = ColorOnCard,
    onBackground    = ColorOnCard,
    error           = ColorCritical
)

@Composable
fun VulnScannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = Typography(),
        content     = content
    )
}
