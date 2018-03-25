package ui.anwesome.com.jshapedview

/**
 * Created by anweshmishra on 25/03/18.
 */
import android.content.*
import android.view.*
import android.graphics.*
class JShapedView(ctx : Context) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}