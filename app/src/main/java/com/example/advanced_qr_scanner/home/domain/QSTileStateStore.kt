package com.example.advanced_qr_scanner.home.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.advanced_qr_scanner.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QSTileStateStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    private object PreferenceConstant {
        const val qsTileToken = "qs_tile_set"
    }

    private object PreferenceKey {
        val qsTileToken = stringPreferencesKey(PreferenceConstant.qsTileToken)
    }

    suspend fun setQsTileToken(state: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferenceKey.qsTileToken] = state.toString()
        }
    }

    suspend fun getQsTileToken(): Boolean? {
        return withContext(dispatcher) {
            return@withContext dataStore.data.firstOrNull()
                ?.get(PreferenceKey.qsTileToken)
                ?.toBooleanStrictOrNull() // Check `true` with case-sensitivity, otherwise null
        }
    }

    fun qsTileToken(): Flow<Boolean?> {
        return dataStore.data.map { prefs ->
            prefs[PreferenceKey.qsTileToken]?.toBooleanStrictOrNull()
        }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(PreferenceKey.qsTileToken) }
    }
}
