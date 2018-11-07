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
    private var mode = 0
    private val mPathList: MutableList<Pair<Float, Float>> = mutableListOf()
    private val mPath: Path = Path()

    private lateinit var mScaleDetector: ScaleGestureDetector
    private lateinit var mGestureDetector: GestureDetector
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

    fun clear(){
        if(::bitmap.isInitialized)
            bitmap.recycle()

        mGestureDetector = GestureDetector(this.context, object: GestureDetector.SimpleOnGestureListener(){
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                this@ColoringView.translationX -= distanceX
                this@ColoringView.translationY -= distanceY
                return true
            }
        })
        mScaleDetector= ScaleGestureDetector(context, object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                mScaleFactor *= detector!!.scaleFactor
                mScaleFactor = max(1f, min(mScaleFactor, 10f))


                this@ColoringView.scaleX = mScaleFactor
                this@ColoringView.scaleY = mScaleFactor

                invalidate()

                return true
            }
        })

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        pencil = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pencil2),
                12, 12, true)
        pencil = changeBitmapColor(pencil, context.getColor(R.color.colorPrimary))
        brush = pencil
//        brush2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pencil2),
//                10, 5, true)

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


//        val paint = Paint()
//        val filter = LightingColorFilter(color, color)
//        paint.colorFilter = filter
//        paint.alpha = 255
//        val canvas = Canvas(resultBitmap)
//        canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
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

    private fun makeEraser(): Bitmap{
        val erase = Bitmap.createBitmap(12, 12, Bitmap.Config.ARGB_8888)

        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        paint.alpha = 0xFF
        val canvas = Canvas(erase)
        canvas.drawBitmap(erase, 0f, 0f, paint)
//        for (i in 0 until erase.width){
//            for(j in 0 until erase.height){
//                val pixel = erase.getPixel(i, j)
//                Log.d("BitmapColor", "$pixel")
//                erase.setPixel(i,j, Color.argb(0, 0, 0, 0))
//            }
//        }
        return erase
    }

    fun changeToErase(){
        mode = 1
//        if(::brush.isInitialized)
//            brush.eraseColor(Color.TRANSPARENT)
    }

    fun changeToPencil(){
        mode = 0
//        if(::brush.isInitialized)
//            brush = pencil
    }
}