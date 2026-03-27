package com.ehansih.vulnscanner.data.models

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-app log collector. All scanner code writes here AND to android.util.Log.
 * UI observes [entries] to show the live debug log screen.
 */
object AppLogger {

    private const val MAX_ENTRIES = 500

    private val _entries = MutableStateFlow<List<LogEntry>>(emptyList())
    val entries: StateFlow<List<LogEntry>> = _entries.asStateFlow()

    fun d(tag: String, msg: String) {
        Log.d(tag, msg)
        append(LogLevel.DEBUG, tag, msg)
    }

    fun i(tag: String, msg: String) {
        Log.i(tag, msg)
        append(LogLevel.INFO, tag, msg)
    }

    fun w(tag: String, msg: String) {
        Log.w(tag, msg)
        append(LogLevel.WARN, tag, msg)
    }

    fun e(tag: String, msg: String, t: Throwable? = null) {
        if (t != null) Log.e(tag, msg, t) else Log.e(tag, msg)
        append(LogLevel.ERROR, tag, msg, t?.stackTraceToString()?.take(800))
    }

    fun clear() = _entries.update { emptyList() }

    fun allText(): String = _entries.value.joinToString("\n") { it.formatted() }

    private fun append(level: LogLevel, tag: String, msg: String, throwable: String? = null) {
        _entries.update { current ->
            val entry = LogEntry(level = level, tag = tag, message = msg, throwable = throwable)
            (current + entry).takeLast(MAX_ENTRIES)
        }
    }
}
