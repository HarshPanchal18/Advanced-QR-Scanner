package com.example.advanced_qr_scanner.scanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.advanced_qr_scanner.R
import com.example.advanced_qr_scanner.databinding.ActivityBarcodeScannerBinding
import com.example.advanced_qr_scanner.extensions.viewBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0
const val BARCODE_QR_CONTENT = "bar-code-qr-content"

@Suppress("DEPRECATION")
class BarcodeScannerActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityBarcodeScannerBinding::inflate)
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var previewUseCase: Preview
    private lateinit var analysisUseCase: ImageAnalysis
    private var flashEnabled = false
    private val screenAspectRatio: Int by lazy { // Initialize with metrics and fetching screen aspect ratio
        val metrics = DisplayMetrics().also { binding.previewView.display?.getRealMetrics(it) }
        aspectRatio(metrics.widthPixels, metrics.heightPixels)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.overlay.post { binding.overlay.setViewFinder() }
        setupCamera()
    }

    private fun setupCamera() {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        // Binding the lifecycle of cameras to LifecycleOwner within an application's process.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCase()
            },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun bindCameraUseCase() {
        bindPreviewUseCase()
        bindAnalyseUseCase()
    }

    private fun bindPreviewUseCase() {
        if (::previewUseCase.isInitialized)
            cameraProvider.unbind(previewUseCase)

        previewUseCase = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        // Send signal to the camera that the previewUseCase is ready to receive data
        previewUseCase.setSurfaceProvider(binding.previewView.surfaceProvider)

        try {
            // Binding the collection of previewUseCase to the lifecycle
            val camera = cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase)
            if (camera.cameraInfo.hasFlashUnit()) {
                binding.ivFlashControl.visibility = View.VISIBLE
                binding.ivFlashControl.setOnClickListener {
                    camera.cameraControl.enableTorch(!flashEnabled)
                }

                camera.cameraInfo.torchState.observe(this) {
                    it?.let { state ->
                        if (state == TorchState.ON) {
                            flashEnabled = true
                            binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_on)
                        } else {
                            flashEnabled = false
                            binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_off)
                        }
                    }
                }
            }
        } catch (stateException: IllegalStateException) {
            stateException.printStackTrace()
        } catch (argException: IllegalArgumentException) {
            argException.printStackTrace()
        }
    }

    private fun bindAnalyseUseCase() {
        val barcodeScanner = BarcodeScanning.getClient()

        if (::analysisUseCase.isInitialized)
            cameraProvider.unbind(analysisUseCase) // Close opened camera

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        // Creating an Executor that uses a single worker thread operating off an unbounded queue
        val cameraExecutor = Executors.newSingleThreadExecutor()

        // Sets an analyzer to receive and analyze images.
        // Setting an analyzer will signal to the camera that it should begin sending data.
        analysisUseCase.setAnalyzer(cameraExecutor) { imageProxy ->
            processImageProxy(barcodeScanner, imageProxy)
        }

        try {
            cameraProvider.bindToLifecycle(this, cameraSelector, analysisUseCase)
        } catch (stateException: IllegalStateException) {
            stateException.printStackTrace()
        } catch (argException: IllegalArgumentException) {
            argException.printStackTrace()
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(inputImage) // detect barcode from provided image
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        val resultIntent = Intent().apply {
                            putExtra(BARCODE_QR_CONTENT, barcode.rawValue)
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
                .addOnFailureListener { it.printStackTrace() }
                .addOnCompleteListener { imageProxy.close() }
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE))
            return AspectRatio.RATIO_4_3
        return AspectRatio.RATIO_16_9
    }
}
