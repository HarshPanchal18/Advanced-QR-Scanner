package com.example.advanced_qr_scanner.home.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import javax.inject.Inject
import comexampleadvancedqrscanner.ScanHistory
import comexampleadvancedqrscanner.ScanHistoryQueries
import kotlinx.coroutines.flow.Flow

class ScanHistoryLocalDataStore @Inject constructor(
    private val queries: ScanHistoryQueries
) {
    fun insert(history: ScanHistory) {
        queries.insert(timeStamp = history.timeStamp, content = history.content)
    }

    fun clear() {
        queries.clear()
    }

    fun fetchAll(): Flow<List<ScanHistory>> = queries.fetchAll().asFlow().mapToList()

}
