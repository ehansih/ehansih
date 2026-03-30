package com.ehansih.vulnscanner.ui.theme

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

// Gradient colors for dashboard
val GradientStart  = Color(0xFF0D1B2A)  // deep navy
val GradientMid    = Color(0xFF1B2838)  // dark steel blue
val GradientEnd    = Color(0xFF0A1628)  // midnight

// Feature card accent colors
val ColorBlue      = Color(0xFF2196F3)
val ColorPurple    = Color(0xFF9C27B0)
val ColorTeal      = Color(0xFF00BCD4)
val ColorOrange    = Color(0xFFFF5722)
val ColorGold      = Color(0xFFFFD700)
val ColorSilver    = Color(0xFFC0C0C0)
val ColorNavy      = Color(0xFF0D47A1)

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
