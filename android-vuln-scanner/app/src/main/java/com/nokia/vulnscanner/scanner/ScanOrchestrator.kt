package com.nokia.vulnscanner.scanner

import android.content.Context
import com.nokia.vulnscanner.data.api.RetrofitClient
import com.nokia.vulnscanner.data.db.CveDatabase
import com.nokia.vulnscanner.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class ScanState {
    object Idle : ScanState()
    data class Scanning(val message: String, val progress: Float) : ScanState()
    data class Done(val summary: ScanSummary) : ScanState()
    data class Error(val message: String) : ScanState()
}

class ScanOrchestrator(private val context: Context) {

    private val db         = CveDatabase.getInstance(context)
    private val appScanner = AppScanner(context, RetrofitClient.nvdApi, db.cveDao())
    private val devScanner = DeviceSecurityScanner(context, RetrofitClient.nvdApi, db.cveDao())
    private val netScanner = NetworkScanner(context)

    fun runFullScan(): Flow<ScanState> = flow {
        emit(ScanState.Scanning("Checking device security…", 0.05f))

        val deviceResult = try {
            devScanner.scan()
        } catch (e: Exception) {
            emit(ScanState.Error("Device scan failed: ${e.message}"))
            return@flow
        }

        emit(ScanState.Scanning("Scanning network…", 0.15f))
        val networkResult = runCatching { netScanner.scan() }.onFailure {
            android.util.Log.e("ScanOrchestrator", "Network scan failed", it)
        }.getOrNull()

        emit(ScanState.Scanning("Scanning installed apps…", 0.20f))
        val appResults = appScanner.scanInstalledApps { current, total, name ->
            val progress = 0.20f + (current.toFloat() / total.toFloat()) * 0.75f
            // We can't emit here directly (suspend), but progress is approximate
        }

        emit(ScanState.Scanning("Calculating risk scores…", 0.97f))

        val criticalCves = appResults.sumOf { r -> r.cves.count { it.severity == Severity.CRITICAL } } +
                           deviceResult.osVulnerabilities.count { it.severity == Severity.CRITICAL }
        val highCves     = appResults.sumOf { r -> r.cves.count { it.severity == Severity.HIGH } } +
                           deviceResult.osVulnerabilities.count { it.severity == Severity.HIGH }
        val vulnerableApps = appResults.count { it.riskScore > 0 }

        val overallRisk = when {
            criticalCves > 0 || deviceResult.securityScore < 40 -> Severity.CRITICAL
            highCves > 3     || deviceResult.securityScore < 60 -> Severity.HIGH
            vulnerableApps > 5                                   -> Severity.MEDIUM
            else                                                 -> Severity.LOW
        }

        val summary = ScanSummary(
            totalApps    = appResults.size,
            vulnerableApps = vulnerableApps,
            criticalCves = criticalCves,
            highCves     = highCves,
            deviceScore  = deviceResult.securityScore,
            overallRisk  = overallRisk,
            appResults   = appResults.sortedByDescending { it.riskScore },
            deviceResult = deviceResult,
            networkResult = networkResult
        )

        emit(ScanState.Done(summary))
    }
}
