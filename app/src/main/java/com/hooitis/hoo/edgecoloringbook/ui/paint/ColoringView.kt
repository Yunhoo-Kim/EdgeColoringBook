package com.hooitis.hoo.edgecoloringbook.ui.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.hooitis.hoo.edgecoloringbook.R
import kotlin.math.abs
import android.graphics.ColorMatrix




@Suppress("unused")
class ColoringView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private lateinit var bitmap:Bitmap
    private val mPath: Path = Path()

    val TOUCH_TOLERANCE = 4


    private var paint: Paint = Paint(Paint.DITHER_FLAG).apply {
        strokeWidth = 12f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        isAntiAlias = true
        alpha = 122
        color = context.getColor(R.color.colorPrimary)
//        maskFilter = EmbossMaskFilter(floatArrayOf(1f,1f,1f), 0.4f, 6f, 3.5f)
    }

    var density: Float = 0.0f
    var mX: Float = 0.0f
    var mY: Float = 0.0f

    private lateinit var canvas: Canvas

    fun blurFilter(){
        paint.maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    fun embossFilter(){
//        paint.maskFilter = ColorMatrixColorFilter(
//                ColorMatrix(floatArrayOf(-1f, 0f, 0f, 0f, 255f, 0f, -1f, 0f, 0f, 255f, 0f, 0f, -1f, 0f, 255f, 0f, 0f, 0f, 1f, 0f)))
        paint.maskFilter = EmbossMaskFilter(floatArrayOf(1f,1f,1f), 0.4f, 6f, 3.5f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clear()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(bitmap, 0f,0f, null)
        canvas.drawPath(mPath, paint)
    }

    fun clear(){
        if(::bitmap.isInitialized)
            bitmap.recycle()

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        paint.apply {
            shader = BitmapShader(
                    BitmapFactory.decodeResource(resources, R.drawable.pencil),
                    Shader.TileMode.REPEAT,
                    Shader.TileMode.REPEAT)
            colorFilter = LightingColorFilter(context.getColor(R.color.colorPrimary),context.getColor(R.color.colorPrimary))
        }
        canvas = Canvas(bitmap)
        invalidate()
    }

    fun touchStart(x: Float, y: Float){
        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    fun touchMove(x: Float, y: Float){
        val dx = abs(x - mX)
        val dy = abs(y - mY)

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    fun touchUp(){
        mPath.lineTo(mX, mY)
        canvas.drawPath(mPath, paint)
        mPath.reset()
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                touchStart(event.x, event.y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(event.x, event.y)
                invalidate()
                return true

            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
                return true
            }
            else -> {
                return false
            }
        }
    }
}