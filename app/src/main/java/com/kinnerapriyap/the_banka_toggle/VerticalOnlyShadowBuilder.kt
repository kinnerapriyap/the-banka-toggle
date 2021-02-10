package com.kinnerapriyap.the_banka_toggle

import android.graphics.Point
import android.view.MotionEvent
import android.view.View

class VerticalOnlyShadowBuilder(view: View, private val motionEvent: MotionEvent) :
    View.DragShadowBuilder(view) {

    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)
        outShadowTouchPoint?.set(motionEvent.x.toInt(), motionEvent.y.toInt())
    }
}