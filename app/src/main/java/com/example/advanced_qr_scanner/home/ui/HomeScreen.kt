package com.example.advanced_qr_scanner.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.advanced_qr_scanner.home.domain.HomeViewModel

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    val hapticFeedback = LocalHapticFeedback.current
    val viewModel: HomeViewModel = viewModel()
}
