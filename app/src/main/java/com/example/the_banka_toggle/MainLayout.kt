package com.example.the_banka_toggle

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper

class MainLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val stretchableSquare: StretchableSquare
        get() = findViewById(R.id.stretchable_square)

    private var dragRange = 0
    private var dragOffset = 0f
    private var yCoordinate = 0
    private var potentiallyAtTop = true
    private var atTop = true

    private val viewDragHelper: ViewDragHelper

    init {
        viewDragHelper = ViewDragHelper.create(this, VerticalOnlyViewDragHelper())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        stretchableSquare.params = StretchableSquareParams.init()
        stretchableSquare.invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        dragRange = measuredHeight - stretchableSquare.height
        stretchableSquare.layout(
            0,
            yCoordinate,
            right,
            yCoordinate + stretchableSquare.measuredHeight
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(
            stretchableSquare.measuredWidth,
            resolveSizeAndState(maxHeight, heightMeasureSpec, 0)
        )
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean =
        viewDragHelper.shouldInterceptTouchEvent(event) &&
                viewDragHelper.isViewUnder(stretchableSquare, event.x.toInt(), event.y.toInt())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        viewDragHelper.processTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                potentiallyAtTop = isInTopHalf(event)
            }
            MotionEvent.ACTION_UP -> {
                atTop = isInTopHalf(event)
                yCoordinate = 0
                if (atTop) smoothSlideToTop()
                else smoothSlideToBottom()
            }
        }
        return viewDragHelper.isViewUnder(stretchableSquare, event.x.toInt(), event.y.toInt())
    }

    private fun isInTopHalf(event: MotionEvent) = event.y < measuredHeight / 2

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun smoothSlideToTop(): Boolean = smoothSlideTo(0f)

    private fun smoothSlideToBottom(): Boolean = smoothSlideTo(1f)

    private fun smoothSlideTo(slideOffset: Float): Boolean {
        if (viewDragHelper.smoothSlideViewTo(
                stretchableSquare,
                0,
                (slideOffset * dragRange).toInt()
            )
        ) {
            ViewCompat.postInvalidateOnAnimation(this)
            return true
        }
        return false
    }

    inner class VerticalOnlyViewDragHelper : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean =
            child == stretchableSquare

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            yCoordinate = top
            dragOffset = top.toFloat() / dragRange

            stretchableSquare.params =
                stretchableSquare.params.copy(
                    paintColor = getPaintColor(potentiallyAtTop),
                )
            stretchableSquare.invalidate()

            requestLayout()
        }

        private fun getPaintColor(potentiallyAtTop: Boolean = true): Int =
            if (potentiallyAtTop) android.R.color.holo_blue_light
            else android.R.color.holo_orange_light

        override fun getViewVerticalDragRange(child: View): Int = dragRange

        private var prevTop = 0
        // = is used so view at extremes doesn't jump to saved _UP position not equal to current
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int =
            when {
                top <= 0 -> 0
                top >= dragRange -> prevTop
                else -> top.apply { prevTop = this }
            }
    }
}