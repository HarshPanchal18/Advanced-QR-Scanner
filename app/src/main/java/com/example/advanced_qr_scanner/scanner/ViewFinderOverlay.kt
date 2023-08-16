package com.example.advanced_qr_scanner.scanner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.advanced_qr_scanner.R

class ViewFinderOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val boxPaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.barcode_reticle_stroke)
        style = Paint.Style.STROKE
        strokeWidth = context.resources.getDimensionPixelOffset(R.dimen.barcode_reticle_stroke_width).toFloat()
    }
    private val scrimPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.barcode_reticle_background)
    }
    private val eraserPaint = Paint().apply {
        strokeWidth = boxPaint.strokeWidth
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private val boxCornerRadius =
        context.resources.getDimensionPixelOffset(R.dimen.barcode_reticle_corner_radius).toFloat()
    private var boxRect: RectF? = null

    fun setViewFinder() {
        val overlayWidth = width.toFloat()
        val overlayHeight = height.toFloat()
        val boxWidth = overlayWidth * 80 / 100 // 80% of the screen width through center aligned
        val boxHeight = overlayHeight * 36 / 100
        val cx = overlayWidth / 2 // CenterX
        val cy = overlayHeight / 2 // CenterY

        boxRect = RectF(
            (cx - boxWidth / 2), // left
            (cy - boxHeight / 2), // top
            (cx + boxWidth / 2), // right
            (cy + boxHeight / 2) // bottom
        )
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        boxRect?.let {
            // Draws the dark background scrim and leaves the box area clear.
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), scrimPaint)

            // As the stroke is always centered,
            // Erase with FILL and STROKE respectively to clear all that area that the box rect would occupy.
            eraserPaint.style = Paint.Style.FILL
            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, eraserPaint)
            eraserPaint.style = Paint.Style.STROKE
            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, eraserPaint)

            // Draws the box
            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, boxPaint)
        }
    }
}
