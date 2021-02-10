package com.kinnerapriyap.the_banka_toggle

import android.graphics.Path
import android.graphics.PointF

fun Path.moveTo(point: PointF) {
    moveTo(point.x, point.y)
}

fun Path.lineTo(point: PointF) {
    lineTo(point.x, point.y)
}

fun Path.quadTo(controlPoint: PointF, endPoint: PointF) {
    quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y)
}

fun Path.addPoint(centerPoint: PointF) {
    addCircle(centerPoint, 5f)
}

fun Path.addCircle(
    centerPoint: PointF,
    radius: Float,
    direction: Path.Direction = Path.Direction.CW
) {
    addCircle(centerPoint.x, centerPoint.y, radius, direction)
}