package com.example.the_banka_toggle

import android.graphics.PointF

data class StretchableSquareBounds(
    val startPositionX: Float = 0f,
    val endPositionX: Float = 0f,
    val startPositionY: Float = 0f,
    val endPositionY: Float = 0f,
    val stretchFactor: Float = 1f
) {
    private val incrementValueX
        get() = (endPositionX - startPositionX) / 10
    private val incrementValueY
        get() = (endPositionY - startPositionY) / 10
    private val midLeftPointX
        get() = startPositionX + 4 * incrementValueX
    private val midRightPointX
        get() = startPositionX + 6 * incrementValueX
    private val controlPointY
        get() = startPositionY + 1 * incrementValueY

    val midPositionX
        get() = (startPositionX + endPositionX) / 2
    val midPositionY
        get() = (startPositionY + endPositionY) / 2

    val topLeadingPoint: PointF
        get() = PointF(startPositionX, startPositionY)
    val topTrailingPoint: PointF
        get() = PointF(endPositionX, startPositionY)
    val controlPointToBottomTrailing: PointF
        get() = getPointFByFactor(
            PointF(endPositionX, controlPointY),
            PointF(midRightPointX, controlPointY),
            stretchFactor
        )
    val bottomTrailingPoint: PointF
        get() = getPointFByFactor(
            PointF(endPositionX, endPositionY),
            PointF(midRightPointX, endPositionY),
            stretchFactor
        )
    val bottomLeadingPoint: PointF
        get() = getPointFByFactor(
            PointF(startPositionX, endPositionY),
            PointF(midLeftPointX, endPositionY),
            stretchFactor
        )
    val controlPointToTopLeading: PointF
        get() = getPointFByFactor(
            PointF(startPositionX, controlPointY),
            PointF(midLeftPointX, controlPointY),
            stretchFactor
        )

    private fun getPointFByFactor(startPoint: PointF, endPoint: PointF, factor: Float): PointF =
        PointF(
            startPoint.x + factor * (endPoint.x - startPoint.x),
            startPoint.y + factor * (endPoint.y - startPoint.y)
        )
}
