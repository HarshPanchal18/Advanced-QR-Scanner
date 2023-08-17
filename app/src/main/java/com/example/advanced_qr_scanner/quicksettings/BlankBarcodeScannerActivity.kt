package com.example.advanced_qr_scanner.quicksettings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.advanced_qr_scanner.extensions.launch
import com.example.advanced_qr_scanner.home.domain.BarcodeContract
import com.example.advanced_qr_scanner.quicksettings.domain.BarcodeScannerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankBarcodeScannerActivity : AppCompatActivity() {

    private val viewModel by viewModels<BarcodeScannerViewModel>()

    private val barcodeLauncher = registerForActivityResult(BarcodeContract()) { result ->
        // Register a request to start an activity for result, designated by the BarcodeContract()
        result?.qrCodeContent?.let { scannedContent ->
            scannedContent.launch(this).also { isLaunched ->
                if (!isLaunched) {
                    val clipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("text", scannedContent)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_LONG).show()
                }
            }
            viewModel.saveContent(scannedContent)
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeLauncher.launch(Unit)
    }
}
