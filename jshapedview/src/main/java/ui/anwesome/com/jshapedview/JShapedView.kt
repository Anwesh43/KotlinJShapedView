package ui.anwesome.com.jshapedview

/**
 * Created by anweshmishra on 25/03/18.
 */
import android.app.Activity
import android.content.*
import android.view.*
import android.graphics.*
class JShapedView(ctx : Context) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Int = 0, var j : Int = 0) {
        val scales : Array<Float> = arrayOf(0f, 0f)
        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir
                if (j == scales.size || j == -1) {
                    j -= dir
                    dir = 0
                    prevScale = scales[j]
                    stopcb(scales[j])
                }
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0) {
                dir = 1 - 2 * prevScale.toInt()
                startcb()
            }
        }
    }
    data class JShaped (var i : Int, val state : State = State()) {
        fun draw(canvas : Canvas, paint : Paint) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            val r = Math.min(w,h)/10
            val START_ANGLE = 150f
            val SWEEP_ANGLE = START_ANGLE
            val END_ANGLE = START_ANGLE - SWEEP_ANGLE
            val LINE_X = r * Math.cos((END_ANGLE) * Math.PI/180).toFloat()
            val LINE_Y = r * Math.sin(END_ANGLE * Math.PI/180).toFloat()
            val SIZE_Y = Math.min(w,h)/3
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeWidth = r/5
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.drawReverseArc(r, START_ANGLE, START_ANGLE - SWEEP_ANGLE * state.scales[0], paint)
            canvas.drawLine(LINE_X, LINE_Y, LINE_X, LINE_Y - SIZE_Y * state.scales[1], paint)
            canvas.restore()
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex : Exception) {

                }
            }
        }
        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
    data class Renderer(var view : JShapedView) {
        val jShaped : JShaped = JShaped(0)
        val animator : Animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            paint.color = Color.YELLOW
            jShaped.draw(canvas, paint)
            animator.animate {
                jShaped.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            jShaped.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity : Activity) : JShapedView {
            val view = JShapedView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
fun Canvas.drawReverseArc(r : Float, start : Float, end : Float, paint : Paint) {
    var  i = start
    paint.style = Paint.Style.STROKE
    val path = Path()
    while (i >= end) {
        val x = r * Math.cos(i * Math.PI/180).toFloat()
        val y = r * Math.sin(i * Math.PI/180).toFloat()
        if (i == start) {
            path.moveTo(x, y)
        }
        else {
            path.lineTo(x, y)
        }
        i--
    }
    drawPath(path, paint)
}