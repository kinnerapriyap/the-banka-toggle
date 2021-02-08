package com.example.the_banka_toggle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class StretchableSquare @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var paintColor: Int
    private var defaultPaintColor: Int = ContextCompat.getColor(context, android.R.color.black)
    private val path = Path()
    private var bounds = StretchableSquareBounds()

    var params: StretchableSquareParams = StretchableSquareParams(1f, 300, true)

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.StretchableSquare)
        paintColor = typedArray.getColor(R.styleable.StretchableSquare_paintColor, 0)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        setup()

        path.moveTo(bounds.topLeadingPoint)
        path.lineTo(bounds.topTrailingPoint)

        if (params.isDebug) {
            path.lineTo(bounds.controlPointToBottomTrailing)
            path.addPoint(bounds.controlPointToBottomTrailing)
            path.moveTo(bounds.topTrailingPoint)
        }
        path.quadTo(bounds.controlPointToBottomTrailing, bounds.bottomTrailingPoint)

        path.lineTo(bounds.bottomLeadingPoint)

        if (params.isDebug) {
            path.lineTo(bounds.controlPointToTopLeading)
            path.addPoint(bounds.controlPointToTopLeading)
            path.moveTo(bounds.bottomLeadingPoint)
        }
        path.quadTo(bounds.controlPointToTopLeading, bounds.topLeadingPoint)

        if (params.shouldInvert()) canvas.scale(1f, -1f, bounds.midPosition, bounds.midPosition)
        canvas.drawPath(path, paint)
    }

    private fun setup() {
        bounds = bounds.copy(
            stretchFactor = params.stretchFactor,
            startPosition = 0f,
            endPosition = params.size.toFloat()
        )
        paint.apply {
            style = if (params.isDebug) Paint.Style.STROKE else Paint.Style.FILL
            strokeWidth = 3f
            if (!params.isDebug) setShadowLayer(10f, 0f, 0f, defaultPaintColor)
            color = if (params.isDebug) defaultPaintColor else paintColor
        }
    }
}