package com.example.the_banka_toggle

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper

class MainLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val stretchableSquare: StretchableSquare
        get() = findViewById(R.id.stretchable_square)
    private val stretchableSquareSize = 300

    private val viewDragHelper: ViewDragHelper

    init {
        viewDragHelper = ViewDragHelper.create(this, VerticalOnlyViewDragHelper())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        stretchableSquare.params = StretchableSquareParams(
            stretchFactor = 0f,
            size = stretchableSquareSize,
            isTop = true,
            isDebug = false
        )
        stretchableSquare.invalidate()
    }

    inner class VerticalOnlyViewDragHelper : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean =
            child == stretchableSquare

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val bottomBound = height - stretchableSquare.height
            return top.coerceAtMost(bottomBound)
        }
    }
}