package com.example.the_banka_toggle

import android.graphics.PointF

data class StretchableSquareBounds(
    val startPosition: Float = 0f,
    val endPosition: Float = 0f,
    val stretchFactor: Float = 1f
) {
    private val incrementValue
        get() = (endPosition - startPosition) / 10
    private val midLeftPointX
        get() = startPosition + 4 * incrementValue
    private val midRightPointX
        get() = startPosition + 6 * incrementValue
    private val controlPointY
        get() = startPosition + 1 * incrementValue
    val midPosition
        get() = (startPosition + endPosition) / 2

    val topLeadingPoint: PointF
        get() = PointF(startPosition, startPosition)
    val topTrailingPoint: PointF
        get() = PointF(endPosition, startPosition)
    val controlPointToBottomTrailing: PointF
        get() = getPointFByFactor(
            PointF(endPosition, controlPointY),
            PointF(midRightPointX, controlPointY),
            stretchFactor
        )
    val bottomTrailingPoint: PointF
        get() = getPointFByFactor(
            PointF(endPosition, endPosition),
            PointF(midRightPointX, endPosition),
            stretchFactor
        )
    val bottomLeadingPoint: PointF
        get() = getPointFByFactor(
            PointF(startPosition, endPosition),
            PointF(midLeftPointX, endPosition),
            stretchFactor
        )
    val controlPointToTopLeading: PointF
        get() = getPointFByFactor(
            PointF(startPosition, controlPointY),
            PointF(midLeftPointX, controlPointY),
            stretchFactor
        )

    private fun getPointFByFactor(startPoint: PointF, endPoint: PointF, factor: Float): PointF =
        PointF(
            startPoint.x + factor * (endPoint.x - startPoint.x),
            startPoint.y + factor * (endPoint.y - startPoint.y)
        )
}
