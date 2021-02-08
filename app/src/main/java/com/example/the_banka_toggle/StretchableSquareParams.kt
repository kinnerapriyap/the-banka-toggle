package com.example.the_banka_toggle

data class StretchableSquareParams(
    var stretchFactor: Float,
    var size: Int,
    var isTop: Boolean,
    var isDebug: Boolean = false
) {
    fun shouldInvert() = !isTop && !isDebug
}