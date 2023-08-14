package com.example.advanced_qr_scanner.home.domain

import comexampleadvancedqrscanner.ScanHistory

sealed class HomeState {
    data class ScanHistoryFetched(val scanHistory: List<ScanHistory>) : HomeState()
    object ScanHistoryEmpty : HomeState()
    object Loading : HomeState()
}
