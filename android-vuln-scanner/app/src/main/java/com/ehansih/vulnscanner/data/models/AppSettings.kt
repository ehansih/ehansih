package com.ehansih.vulnscanner.data.models

import android.content.Context
import android.content.SharedPreferences

/**
 * Simple SharedPreferences wrapper for user-configurable settings.
 * API keys are stored locally on device only.
 */
class AppSettings(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("vulnscanner_settings", Context.MODE_PRIVATE)

    var virusTotalApiKey: String
        get() = prefs.getString(KEY_VT_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_VT_API_KEY, value).apply()

    var hibpApiKey: String
        get() = prefs.getString(KEY_HIBP_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_HIBP_API_KEY, value).apply()

    var scheduledScanEnabled: Boolean
        get() = prefs.getBoolean(KEY_SCHEDULED_SCAN, false)
        set(value) = prefs.edit().putBoolean(KEY_SCHEDULED_SCAN, value).apply()

    var scheduledScanIntervalDays: Int
        get() = prefs.getInt(KEY_SCAN_INTERVAL, 1)
        set(value) = prefs.edit().putInt(KEY_SCAN_INTERVAL, value).apply()

    val hasVirusTotalKey: Boolean get() = virusTotalApiKey.isNotBlank()
    val hasHibpKey: Boolean get() = hibpApiKey.isNotBlank()

    companion object {
        private const val KEY_VT_API_KEY    = "vt_api_key"
        private const val KEY_HIBP_API_KEY  = "hibp_api_key"
        private const val KEY_SCHEDULED_SCAN = "scheduled_scan"
        private const val KEY_SCAN_INTERVAL = "scan_interval_days"

        @Volatile private var instance: AppSettings? = null

        fun getInstance(context: Context): AppSettings =
            instance ?: synchronized(this) {
                instance ?: AppSettings(context.applicationContext).also { instance = it }
            }
    }
}
