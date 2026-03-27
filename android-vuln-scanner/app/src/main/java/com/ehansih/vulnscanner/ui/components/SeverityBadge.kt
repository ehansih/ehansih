package com.ehansih.vulnscanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ehansih.vulnscanner.data.models.Severity
import com.ehansih.vulnscanner.ui.theme.*

fun severityColor(s: Severity): Color = when (s) {
    Severity.CRITICAL -> ColorCritical
    Severity.HIGH     -> ColorHigh
    Severity.MEDIUM   -> ColorMedium
    Severity.LOW      -> ColorLow
    Severity.INFO     -> ColorInfo
}

fun severityLabel(s: Severity): String = when (s) {
    Severity.CRITICAL -> "CRITICAL"
    Severity.HIGH     -> "HIGH"
    Severity.MEDIUM   -> "MEDIUM"
    Severity.LOW      -> "LOW"
    Severity.INFO     -> "INFO"
}

@Composable
fun SeverityBadge(severity: Severity, modifier: Modifier = Modifier) {
    val color = severityColor(severity)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.20f))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = severityLabel(severity),
            color      = color,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ScoreGauge(score: Int, modifier: Modifier = Modifier) {
    val color = when {
        score >= 80 -> ColorOk
        score >= 60 -> ColorLow
        score >= 40 -> ColorMedium
        score >= 20 -> ColorHigh
        else        -> ColorCritical
    }
    val label = when {
        score >= 80 -> "SECURE"
        score >= 60 -> "FAIR"
        score >= 40 -> "AT RISK"
        score >= 20 -> "HIGH RISK"
        else        -> "CRITICAL"
    }
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text       = "$score",
            color      = color,
            fontSize   = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text     = label,
            color    = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
    }
}
