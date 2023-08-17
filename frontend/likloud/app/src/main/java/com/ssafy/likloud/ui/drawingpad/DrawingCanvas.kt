package com.ssafy.likloud.ui.drawingpad

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.bitmap
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.bitmapCanvas
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.paint
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.points
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.selectedColor
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.selectedEraserStrokeWidth
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.selectedStrokeWidth

private const val TAG = "DrawingCanvas_싸피"

class DrawingCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var desiredHeight: Int = 0


    private fun initBitmap(width: Int, height: Int) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap!!)
    }


//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        // Set the measured height to the desired height.
//        val width = measuredWidth
//        val height = resolveSize(desiredHeight, heightMeasureSpec)
//        setMeasuredDimension(100, 1000)
//    }


//    fun setCanvasHeight(height: Int) {
//        desiredHeight = height
//        setMeasuredDimension(measuredWidth, 1)
//        requestLayout() // Request a layout update to apply the new height.
//    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0)
            initBitmap(w, h)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(selectedColor != Color.TRANSPARENT)
                points.add(Point(event.x, event.y, false, selectedColor, selectedStrokeWidth))
                else
                    points.add(Point(event.x, event.y, false, selectedColor, selectedEraserStrokeWidth))

            }

            MotionEvent.ACTION_MOVE -> {
                if(selectedColor != Color.TRANSPARENT)
                    points.add(Point(event.x, event.y, true, selectedColor, selectedStrokeWidth))
                else
                    points.add(Point(event.x, event.y, true, selectedColor, selectedEraserStrokeWidth))
            }

            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "onTouchEvent: up")

            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLinesOnCanvas()
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
    }

    private fun drawLinesOnCanvas() {
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        points.forEachIndexed { index, point ->
            if (index >= 1 && point.isContinue) {
                updatePaintForPoint(points[index])
                bitmapCanvas?.drawLine(
                    points[index - 1].x,
                    points[index - 1].y,
                    point.x,
                    point.y,
                    paint
                )
            }
        }
    }

    private fun updatePaintForPoint(point: Point) {
        paint.color = point.color
        paint.strokeWidth = point.strokeWidth
        paint.xfermode = if (paint.color == Color.TRANSPARENT) {
            PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else null
    }
}