package com.kinnerapriyap.the_banka_toggle

import android.graphics.Color

data class StretchableSquareParams(
    var stretchFactor: Float,
    var atTop: Boolean,
    val scaleForTranslation: Float,
    var isDebug: Boolean = false
) {
    companion object {
        const val stretchableSquareSize = 300
        const val debugPaintColor: Int = Color.BLACK
        val defaultPaintColorTop = Color.parseColor("#ff33b5e5")
        val defaultPaintColorBottom = Color.parseColor("#ffffbb33")
        fun init(): StretchableSquareParams = StretchableSquareParams(
            stretchFactor = 0f,
            atTop = true,
            scaleForTranslation = 1f,
            isDebug = false
        )
    }

    fun shouldInvert() = !atTop && !isDebug
}