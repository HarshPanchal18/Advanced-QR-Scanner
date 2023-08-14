package com.example.advanced_qr_scanner.home.domain

import androidx.lifecycle.ViewModel
import com.example.advanced_qr_scanner.home.domain.usecase.ClearScanHistoryUseCase
import com.example.advanced_qr_scanner.home.domain.usecase.FetchScanHistoryUseCase
import com.example.advanced_qr_scanner.home.domain.usecase.SaveScanHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clearScanHistory: ClearScanHistoryUseCase,
    private val saveScanHistory: SaveScanHistoryUseCase,
    private val fetchScanHistory: FetchScanHistoryUseCase,
): ViewModel() {
}
