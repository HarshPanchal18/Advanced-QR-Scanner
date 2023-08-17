package com.example.advanced_qr_scanner.home.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import com.example.advanced_qr_scanner.theme.AdvancedQRScannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvancedQRScannerTheme {
                HomeScreen()
                var pressedTime: Long = 0
                BackHandler(enabled = true) {
                    if(pressedTime + 2000 > System.currentTimeMillis()) finish()
                    else Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT)
                        .show()
                    pressedTime = System.currentTimeMillis()
                }
            }
        }
    }
}
