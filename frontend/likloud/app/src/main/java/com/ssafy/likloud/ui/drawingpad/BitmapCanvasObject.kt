package com.ssafy.likloud.ui.drawingpad

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

object BitmapCanvasObject {
    val paint = Paint()
    var points = ArrayDeque<Point>()
    var erasedPoints = ArrayDeque<Point>()
    var clearedPoints = ArrayDeque<Point>()
    var isCleared = false
    var selectedColor = Color.BLACK
    var selectedStrokeWidth = 1f
    var selectedEraserStrokeWidth = 1f
    var bitmap: Bitmap? = null
    var bitmapCanvas: Canvas? = null

}