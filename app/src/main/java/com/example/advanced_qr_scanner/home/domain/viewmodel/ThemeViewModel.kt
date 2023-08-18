package com.example.advanced_qr_scanner.home.domain.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    companion object {
        var isDarkThemeEnabled = mutableStateOf(false)
            private set
    }

    /*fun setTheme(isDarkTheme: Boolean) {
        isDarkThemeEnabled.value = isDarkTheme
    }*/
}
