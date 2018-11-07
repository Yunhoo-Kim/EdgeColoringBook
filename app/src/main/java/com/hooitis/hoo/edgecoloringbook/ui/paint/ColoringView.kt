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
import android.util.Log


@Suppress("unused")
class ColoringView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private lateinit var bitmap:Bitmap
    private lateinit var brush: Bitmap
    private lateinit var pencil: Bitmap
    private lateinit var originPencil: Bitmap
    private var mode = 0
    private val mPathList: MutableList<Pair<Float, Float>> = mutableListOf()
    private val mPath: Path = Path()

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

//    fun blurFilter(){
//        paint.maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
//    }
//
//    private fun embossFilter(){
//        paint.colorFilter = ColorMatrixColorFilter(
//                ColorMatrix(floatArrayOf(0f, 0f, 0f, 0f, 300f, 0f, 1f, 0f, 0f, 300f, 0f, 0f, 1f, 0f, 300f, 0f, 0f, 0f, 1f, 0f)))
//    }

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
        pencil = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pencil2),
                12, 12, true)
        pencil = changeBitmapColor(pencil, context.getColor(R.color.colorPrimary))
        brush = pencil
        originPencil = pencil

        canvas = Canvas(bitmap)
        invalidate()
    }

    private fun touchStart(x: Float, y: Float){
        mPathList.clear()
        mX = x
        mY = y
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

        if(mPathList.size < 10) {
            return
        }

        var paint: Paint? = null

        if(mode==1){
            paint = eraser
        }

        for(i in mPathList){
            canvas.drawBitmap(brush, i.first, i.second, paint)
        }

        mPathList.clear()
    }

    private fun touchUp(){
        drawBitmap()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointerCount = event!!.pointerCount

        var action = event.action

        if(pointerCount > 1){
            multiTouching = true
            return false
        }

        if(multiTouching)
            action = MotionEvent.ACTION_DOWN

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(event.x, event.y)
                invalidate()
                multiTouching = false
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

        if(multiTouching)
            mPathList.clear()

        return true
    }

    private fun changeBitmapColor(sourceBitmap: Bitmap, color: Int): Bitmap {
        val resultBitmap = sourceBitmap.copy(sourceBitmap.config, true)
        resultBitmap.setHasAlpha(true)

        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)

        for (i in 0 until resultBitmap.width){
            for(j in 0 until resultBitmap.height){
                val pixel = resultBitmap.getPixel(i, j)
                Log.d("BitmapColor", "$pixel")
                resultBitmap.setPixel(i,j, Color.argb(Color.alpha(pixel), red, green, blue))
            }
        }
        return resultBitmap
    }

    fun changeToErase(){
        mode = 1
    }

    fun changeToPencil(){
        mode = 0
    }

    fun brushScale(scaleFactor: Float){
        if(!::pencil.isInitialized)
            return

        val scaleBrush = max(6f, min((12 * (1 / scaleFactor)), 12f)).toInt()

        pencil = Bitmap.createScaledBitmap(originPencil,
                scaleBrush, scaleBrush, true)
        brush = pencil
    }
}