package com.hooitis.hoo.edgecoloringbook.ui.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.hooitis.hoo.edgecoloringbook.R
import kotlin.math.abs
import android.view.*
import kotlin.math.max
import kotlin.math.min
import android.graphics.Bitmap
import android.widget.ImageView
import com.hooitis.hoo.edgecoloringbook.utils.DRAWING_MODE


@Suppress("unused")
class ColoringView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private lateinit var bitmap:Bitmap
    private lateinit var brush: Bitmap
    private lateinit var pencil: Bitmap
    private lateinit var originPencil: Bitmap
    private var mode = 0
    private var drawingMode = 0
    private val mPathList: MutableList<Pair<Float, Float>> = mutableListOf()
    private var color: Int = 0
    private var scaleBrush = 2
    private var mScaleFactor = 1f

    val TOUCH_TOLERANCE = 4

    private var eraser: Paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        alpha = 0
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 10f
        isAntiAlias = true
    }

    var density: Float = 0.0f
    private var mX: Float = 0.0f
    private var mY: Float = 0.0f
    private var multiTouching = false

    private lateinit var canvas: Canvas


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clear()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(bitmap, 0f,0f, null)
    }

    private fun clear(){
        if(::bitmap.isInitialized)
            bitmap.recycle()


        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        originPencil = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pencil2),
        originPencil = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pencil3),
                50, 50, true)

        pencil = originPencil
        brushScale(12f)
        color = context.getColor(R.color.black)
        changeBitmapColor(color)

        canvas = Canvas(bitmap)
        invalidate()
    }

    private fun touchStart(x: Float, y: Float){
        mPathList.clear()
        mX = x
        mY = y
        mPathList.add(Pair(x, y))
    }

    private fun touchMove(x: Float, y: Float){
        val dx = abs(x - mX)
        val dy = abs(y - mY)

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            drawLine(Point(mX.toInt(), mY.toInt()), Point(x.toInt(), y.toInt()))
            mX = x
            mY = y
        }

        drawBitmap()
    }

    private fun drawLine(p1: Point, p2: Point){
        val dx = abs(p1.x - p2.x)
        val dy = abs(p1.y - p2.y)
        val sx = if(p1.x < p2.x) 1 else -1
        val sy = if(p1.y < p2.y) 1 else -1
        var xx = p1.x
        var yy = p1.y

        var e1 = (if(dx > dy) dx else -dy) / 2
        var e2: Int

        while(true){
            mPathList.add(Pair(xx.toFloat(), yy.toFloat()))
            if(xx == p2.x && yy == p2.y) break
            e2 = e1
            if(e2 > -dx){e1 -= dy; xx += sx}
            if(e2 < dy){e1 += dx; yy += sy}
        }
    }

    private fun drawBitmap(){
        var paint: Paint? = null

        if(mode == 1){
            paint = eraser
        }

        val f = scaleBrush / 2


        for(i in mPathList){
            canvas.drawBitmap(brush, i.first, i.second - f, paint)
        }

        mPathList.clear()
    }

    private fun touchUp(){
        drawBitmap()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(drawingMode != DRAWING_MODE)
            return false

        val action = event!!.action

//        val pointerCount = event.pointerCount
//        if(pointerCount > 1){
//            return false
//        }
//
//        if(multiTouching)
//            action = MotionEvent.ACTION_DOWN

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(event.x, event.y)
                invalidate()
//                multiTouching = false
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
            else -> { }
        }

//        if(multiTouching)
//            mPathList.clear()

        return true
    }

    fun changeBitmapColor(color: Int) {
        if(!::pencil.isInitialized)
            return

        this.color = color
        val resultBitmap = pencil.copy(pencil.config, true)
        resultBitmap.setHasAlpha(true)

        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)

        for (i in 0 until resultBitmap.width){
            for(j in 0 until resultBitmap.height){
                val pixel = resultBitmap.getPixel(i, j)
                resultBitmap.setPixel(i,j, Color.argb(Color.alpha(pixel), red, green, blue))
            }
        }

        pencil = resultBitmap
        brush = pencil
    }

    fun changeToErase(){
        mode = 1
    }

    fun changeToPencil(){
        mode = 0
    }

    fun setDrawingMode(drawingMode: Int){
        this.drawingMode = drawingMode
    }

    fun brushScale(scaleFactor: Float){
        if(!::pencil.isInitialized)
            return

        scaleBrush = max(1f, min((scaleFactor), 50f)).toInt()
        pencil = Bitmap.createScaledBitmap(originPencil,
                scaleBrush, scaleBrush, true)

        changeBitmapColor(color)
    }
}