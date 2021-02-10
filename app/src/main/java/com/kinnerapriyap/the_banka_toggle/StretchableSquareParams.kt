package com.kinnerapriyap.the_banka_toggle

import android.content.Context
import androidx.core.content.ContextCompat

data class StretchableSquareParams(
    var stretchFactor: Float,
    var size: Int,
    var atTop: Boolean,
    val paintColor: Int,
    val scaleForTranslation: Float,
    var isDebug: Boolean = false
) {
    companion object {
        const val stretchableSquareSize = 300
        fun init(): StretchableSquareParams = StretchableSquareParams(
            stretchFactor = 0f,
            size = stretchableSquareSize,
            atTop = true,
            paintColor = android.R.color.holo_blue_light,
            scaleForTranslation = 1f,
            isDebug = false
        )
    }

    fun shouldInvert() = !atTop && !isDebug

    fun getPaintColor(context: Context) = ContextCompat.getColor(context, paintColor)

    fun getWidth() = size

    fun getHeight() = (size * scaleForTranslation).toInt()
}