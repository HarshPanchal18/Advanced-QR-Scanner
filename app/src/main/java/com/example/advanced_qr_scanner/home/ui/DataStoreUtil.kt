package com.example.advanced_qr_scanner.home.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreUtil(private val context: Context) {

    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings")
        val THEME_KEY = booleanPreferencesKey("theme")
    }

    fun getTheme(isSystemDarkTheme: Boolean): Flow<Boolean> = context.datastore.data.map { prefs ->
        prefs[THEME_KEY] ?: isSystemDarkTheme
    }

    suspend fun saveTheme(isSystemDarkTheme: Boolean) {
        context.let {
            it.datastore.edit { prefs ->
                prefs[THEME_KEY] = isSystemDarkTheme
            }
        }
    }
}
