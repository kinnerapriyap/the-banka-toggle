package com.example.the_banka_toggle

import android.content.Context
import androidx.core.content.ContextCompat

data class StretchableSquareParams(
    var stretchFactor: Float,
    var size: Int,
    var isTop: Boolean,
    val paintColor: Int,
    var isDebug: Boolean = false
) {
    fun shouldInvert() = !isTop && !isDebug

    fun getPaintColor(context: Context) = ContextCompat.getColor(context, paintColor)
}