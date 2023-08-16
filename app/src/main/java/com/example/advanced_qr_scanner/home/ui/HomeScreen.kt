package com.example.advanced_qr_scanner.home.ui

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.advanced_qr_scanner.R
import com.example.advanced_qr_scanner.extensions.launch
import com.example.advanced_qr_scanner.home.domain.BarcodeContract
import com.example.advanced_qr_scanner.home.domain.HomeState
import com.example.advanced_qr_scanner.home.domain.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import comexampleadvancedqrscanner.ScanHistory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    val hapticFeedback = LocalHapticFeedback.current
    val viewModel: HomeViewModel = viewModel()
    val bottomSheetScaffoldState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = BarcodeContract(),
    ) { result ->
        result.qrCodeContent?.let { scannedData ->
            scannedData.launch(context).also { isLaunched ->
                if (!isLaunched) {
                    localClipboardManager.setText(AnnotatedString(scannedData))
                    Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_LONG).show()
                }
            }
            viewModel.saveContent(content = scannedData)
        }
    }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                barcodeLauncher.launch(Unit)
            } else {
                coroutineScope.launch { bottomSheetScaffoldState.show() }
            }
        })
    viewModel.loadScanHistory()

    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(32.dp),
        sheetElevation = 8.dp,
        sheetContent = {
            PermissionBottomSheet(
                title = "Camera permission required",
                subTitle = "Permission for camera required for scanning barcode",
                buttonText = "Allow"
            ) {
                cameraPermissionState.launchPermissionRequest()
                coroutineScope.launch { bottomSheetScaffoldState.hide() }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TitleBar()
            ScanQRCode {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                        barcodeLauncher.launch(Unit)
                    }

                    else -> {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }
            }
            ScanHistoryList(
                homeState = viewModel.homeState,
                onItemClick = { history ->
                    history.content.launch(context)
                },
                onClearClick = { viewModel.removeAllScanHistory() },
                onItemLongPress = { history ->
                    localClipboardManager.setText(AnnotatedString(history.content))
                    Toast.makeText(context,"Copied to clipboard",Toast.LENGTH_SHORT).show()
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        }
    }
}

@Composable
fun TitleBar(modifier: Modifier = Modifier) {
    Text(
        text = "Scanner",
        fontSize = 20.sp,
        fontWeight = FontWeight.W700,
        modifier = modifier.padding(start = 16.dp, top = 26.dp)
    )
}

@Composable
fun ScanQRCode(
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onScanClick() }
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Rounded.QrCodeScanner,
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Scan a QR/Bar code",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
fun ScanHistoryList(
    modifier: Modifier = Modifier,
    homeState: HomeState,
    onItemClick: (ScanHistory) -> Unit,
    onItemLongPress: (ScanHistory) -> Unit,
    onClearClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        when (homeState) {
            is HomeState.Loading -> {
                Title()
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(24.dp)
                        .size(56.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            is HomeState.ScanHistoryEmpty -> {
                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.scan))
                val animation by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
                Title()
                LottieAnimation(
                    composition = composition, progress = { animation },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Start by scanning a barcode,\nusing the button below",
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            is HomeState.ScanHistoryFetched -> {
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy hh.mm.ss aa", Locale.ENGLISH)

                TitleWithClear { onClearClick() }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(homeState.scanHistory) { item ->
                        ScanHistoryItem(
                            item = item,
                            modifier = Modifier.fillMaxWidth(),
                            dateFormatter = dateFormatter,
                            onClick = onItemClick,
                            onLongClick = onItemLongPress
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "Previously Scanned Items",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    )
}

@Composable
fun TitleWithClear(onClearClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (title, clearButton) = createRefs()
        Text(
            text = "Previously scanned items",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(clearButton.start)
                    width = Dimension.fillToConstraints
                }
        )
        TextButton(
            onClick = { onClearClick() },
            modifier = Modifier.constrainAs(clearButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) { Text(text = "Clear") }
    }
}

@Composable
fun ScanHistoryItem(
    modifier: Modifier = Modifier,
    item: ScanHistory,
    dateFormatter: SimpleDateFormat,
    onClick: (ScanHistory) -> Unit,
    onLongClick: (ScanHistory) -> Unit
) {
    Card(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { onClick(item) },
                    onLongPress = { onLongClick(item) }
                )
            }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(
                    text = item.content,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )
                Text(
                    text = dateFormatter.format(item.timeStamp),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanHistoryListPreview() {
    ScanHistoryList(homeState = HomeState.ScanHistoryFetched(listOf()),
        onItemClick = {},
        onItemLongPress = {},
        onClearClick = {}
    )
}
