package com.kinnerapriyap.the_banka_toggle

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import kotlin.math.abs

class MainLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val stretchableSquare: StretchableSquare
        get() = findViewById(R.id.stretchable_square)

    private var potentiallyAtTop = true
    private var stuck = true
    private var justUnstuck = false

    // dp
    private var yCoordinate = 0
    private val stickyThreshold
        get() =
            if (potentiallyAtTop) measuredHeight / 3
            else measuredHeight / 3 + stretchableSquare.size / 2
    private val scaleForTranslation: Float
        get() =
            if (stuck || justUnstuck)
                1f + absTranslation / stretchableSquare.size
            else 1f
    private val stretchFactor
        get() = if (stuck || justUnstuck) absTranslation / stickyThreshold else 0f
    private val dragRange
        get() = measuredHeight - stretchableSquare.size

    private val topInLayout: Int
        get() = if (stuck && potentiallyAtTop) 0 else yCoordinate
    private val bottomInLayout: Int
        get() =
            if (stuck && potentiallyAtTop) stretchableSquare.measuredHeight
            else yCoordinate + stretchableSquare.measuredHeight

    // px
    private var yTranslation = 0f
    private val absTranslation
        get() = abs(yTranslation)

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
        stretchableSquare.layout(0, topInLayout, right, bottomInLayout)
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
                yTranslation =
                    if (potentiallyAtTop) event.y
                    else event.y - measuredHeight
                if (stuck) {
                    stuck = absTranslation < stickyThreshold
                    justUnstuck = !stuck
                }
            }
            MotionEvent.ACTION_UP -> {
                potentiallyAtTop = isInTopHalf(event)
                yTranslation = 0f
                stuck = true
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
            when {
                justUnstuck -> {
                    springBack()
                    justUnstuck = false
                }
                isAnimating -> return
                else -> updateStretchableSquareView(stretchFactor, scaleForTranslation)
            }
        }

        var isAnimating = false

        private fun springBack() {
            isAnimating = true
            val startPosition = if (potentiallyAtTop) 0f else dragRange.toFloat()
            val finalPosition =
                if (potentiallyAtTop) stickyThreshold
                else stickyThreshold + stretchableSquare.size / 3
            AnimatorSet().apply {
                play(
                    ObjectAnimator.ofFloat(
                        stretchableSquare,
                        View.Y,
                        startPosition,
                        finalPosition.toFloat()
                    )
                ).apply {
                    with(
                        ObjectAnimator.ofFloat(
                            stretchableSquare,
                            "stretchFactor",
                            stretchFactor,
                            0f
                        )
                    )
                    with(
                        ObjectAnimator.ofFloat(
                            stretchableSquare,
                            "scaleForTranslation",
                            scaleForTranslation,
                            1f
                        )
                    )
                }
                duration = 200
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        isAnimating = false
                    }
                })
                start()
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            updateStretchableSquareView(stretchFactor = 0f, scale = 1f)

            val finalPosition = if (potentiallyAtTop) 0 else dragRange
            SpringAnimation(stretchableSquare, DynamicAnimation.Y, finalPosition.toFloat()).apply {
                setStartVelocity(yvel)
                spring.dampingRatio = 0.4f
                start()
            }
            viewDragHelper.settleCapturedViewAt(0, finalPosition)
            postInvalidateOnAnimation()
        }

        private fun updateStretchableSquareView(stretchFactor: Float, scale: Float) {
            stretchableSquare.params =
                stretchableSquare.params.copy(
                    stretchFactor = stretchFactor,
                    atTop = potentiallyAtTop,
                    scaleForTranslation = scale,
                )
            stretchableSquare.invalidate()
            stretchableSquare.requestLayout()
            requestLayout()
        }

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