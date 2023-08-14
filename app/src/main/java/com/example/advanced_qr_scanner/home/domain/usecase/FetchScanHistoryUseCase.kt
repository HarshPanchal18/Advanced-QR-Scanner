package com.example.advanced_qr_scanner.home.domain.usecase

import com.example.advanced_qr_scanner.home.domain.ScanHistoryRepo
import javax.inject.Inject

class FetchScanHistoryUseCase @Inject constructor(private val repo: ScanHistoryRepo) {
    operator fun invoke() = repo.fetchScanHistory()
}
