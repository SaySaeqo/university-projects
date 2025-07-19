package mso.lab

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import java.util.TreeSet

/**
 * TODO: document your custom view class.
 */
class DrawingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val points = ArrayList<PointF>()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    points.add(PointF(it.x, it.y))
                    invalidate()
                    return true
                }
                else -> {
                    // Do nothing for other actions
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 5f
        for (p in points){
            canvas?.let {it.drawCircle(p.x,p.y, 100f, paint)}
        }
    }



}