package com.nokia.vulnscanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nokia.vulnscanner.data.models.ScanSummary
import com.nokia.vulnscanner.scanner.ScanOrchestrator
import com.nokia.vulnscanner.scanner.ScanState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ScanUiState(
    val isScanning: Boolean     = false,
    val progress: Float         = 0f,
    val message: String         = "",
    val summary: ScanSummary?   = null,
    val error: String?          = null
)

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    private val orchestrator = ScanOrchestrator(application)

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun startScan() {
        if (_uiState.value.isScanning) return
        _uiState.update { it.copy(isScanning = true, error = null, summary = null) }

        viewModelScope.launch {
            orchestrator.runFullScan().collect { state ->
                when (state) {
                    is ScanState.Scanning -> _uiState.update {
                        it.copy(progress = state.progress, message = state.message)
                    }
                    is ScanState.Done     -> _uiState.update {
                        it.copy(isScanning = false, summary = state.summary, progress = 1f)
                    }
                    is ScanState.Error    -> _uiState.update {
                        it.copy(isScanning = false, error = state.message)
                    }
                    ScanState.Idle        -> {}
                }
            }
        }
    }

    fun dismissError() = _uiState.update { it.copy(error = null) }
}
