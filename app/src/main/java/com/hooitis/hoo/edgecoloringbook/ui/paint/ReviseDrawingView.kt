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
import com.hooitis.hoo.edgecoloringbook.utils.DRAWING_MODE


@Suppress("unused")
class ReviseDrawingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private lateinit var bitmap:Bitmap
    private lateinit var brush: Bitmap
    private lateinit var pencil: Bitmap
    private lateinit var originPencil: Bitmap
    private var mode = 0
    private var drawingMode = DRAWING_MODE
    private val mPathList: MutableList<Pair<Float, Float>> = mutableListOf()
    private val mPath: Path = Path()
    private var color: Int = 0

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
    private var mPaint: Paint = Paint().apply {
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
        originPencil = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pencil2),
                15, 15, true)

        pencil = originPencil
        color = context.getColor(R.color.colorPrimary)
        changeBitmapColor(color)
//        brush = pencil

        canvas = Canvas(bitmap)
        invalidate()
    }

    private fun touchStart(x: Float, y: Float){
        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float){
        val dx = abs(x - mX)
        val dy = abs(y - mY)

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            mPath.quadTo(mX,mY, (x + mX) / 2, (y + mY)/ 2)
            mX = x
            mY = y
        }
        drawPaint()
    }

    private fun drawPaint(){
        canvas.drawPath(mPath, mPaint)
        mPath.reset()
    }

    private fun touchUp(){
        mPath.lineTo(mX, mY)
        drawPaint()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(drawingMode != DRAWING_MODE)
            return false

        val action = event!!.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(event.x, event.y)
                invalidate()
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
        return true
    }

    fun changeBitmapColor(color: Int): Bitmap {
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
        return resultBitmap
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

        val scaleBrush = max(5f, min((15 * (1 / scaleFactor)), 15f)).toInt()
        pencil = Bitmap.createScaledBitmap(originPencil,
                scaleBrush, scaleBrush, true)

        changeBitmapColor(color)
    }
}