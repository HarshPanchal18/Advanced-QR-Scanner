package com.example.advanced_qr_scanner.quicksettings

import android.content.Intent
import android.service.quicksettings.TileService
import com.example.advanced_qr_scanner.home.domain.QSTileStateStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScannerQsTile : TileService() {

    @Inject
    lateinit var tileStateStore: QSTileStateStore

    override fun onTileAdded() {
        super.onTileAdded()
        CoroutineScope(Dispatchers.IO).launch {
            tileStateStore.setQsTileToken(true)
        }
    }

    override fun onClick() {
        super.onClick()
        Intent(this,BlankBarcodeScannerActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivityAndCollapse(this)
        }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        CoroutineScope(Dispatchers.IO).launch {
            tileStateStore.setQsTileToken(false)
        }
    }
}
