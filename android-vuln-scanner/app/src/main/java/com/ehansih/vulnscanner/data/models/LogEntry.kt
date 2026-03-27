package com.ehansih.vulnscanner.data.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LogLevel { DEBUG, INFO, WARN, ERROR }

data class LogEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: String? = null
) {
    fun formatted(): String {
        val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date(timestamp))
        val lvl  = level.name.first()
        val base = "$time [$lvl] $tag: $message"
        return if (throwable != null) "$base\n$throwable" else base
    }
}
