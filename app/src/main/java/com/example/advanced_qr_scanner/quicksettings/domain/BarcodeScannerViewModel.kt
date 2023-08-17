package com.example.advanced_qr_scanner.quicksettings.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advanced_qr_scanner.home.domain.usecase.SaveScanHistoryUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class BarcodeScannerViewModel @Inject constructor(
    private val saveScanHistory: SaveScanHistoryUseCase
) : ViewModel() {

    fun saveContent(content: String) {
        viewModelScope.launch {
            saveScanHistory(content)
        }
    }
}
