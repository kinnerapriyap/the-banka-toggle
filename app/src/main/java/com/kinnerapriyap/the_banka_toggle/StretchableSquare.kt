package com.kinnerapriyap.the_banka_toggle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.kinnerapriyap.the_banka_toggle.StretchableSquareParams.Companion.defaultPaintColorTop
import com.kinnerapriyap.the_banka_toggle.StretchableSquareParams.Companion.defaultPaintColorBottom
import com.kinnerapriyap.the_banka_toggle.StretchableSquareParams.Companion.debugPaintColor
import com.kinnerapriyap.the_banka_toggle.StretchableSquareParams.Companion.stretchableSquareSize

class StretchableSquare @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var bounds = StretchableSquareBounds()

    var params: StretchableSquareParams = StretchableSquareParams.init()
    private val paintColorPair: Pair<Int, Int>
    val size: Int

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.StretchableSquare)
        paintColorPair =
            typedArray.getColor(
                R.styleable.StretchableSquare_paintColorTop,
                defaultPaintColorTop
            ) to
                    typedArray.getColor(
                        R.styleable.StretchableSquare_paintColorBottom,
                        defaultPaintColorBottom
                    )
        size = typedArray.getInt(R.styleable.StretchableSquare_size, stretchableSquareSize)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        path.rewind()
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

        if (params.shouldInvert()) canvas.scale(1f, -1f, bounds.midPositionX, bounds.midPositionY)
        canvas.drawPath(path, paint)
    }

    private fun setup() {
        bounds = bounds.copy(
            stretchFactor = params.stretchFactor,
            startPositionX = 0f,
            endPositionX = measuredWidth.toFloat(),
            startPositionY = 0f,
            endPositionY = measuredHeight.toFloat()
        )
        paint.apply {
            style = if (params.isDebug) Paint.Style.STROKE else Paint.Style.FILL
            strokeWidth = 3f
            if (!params.isDebug) setShadowLayer(10f, 0f, 0f, debugPaintColor)
            color = when {
                params.isDebug -> debugPaintColor
                params.atTop -> paintColorPair.first
                else -> paintColorPair.second
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
        setMeasuredDimension(size, (size * params.scaleForTranslation).toInt())
}