package com.hooitis.hoo.edgecoloringbook.ui.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import kotlin.math.abs
import android.view.*
import kotlin.math.max
import kotlin.math.min
import android.graphics.Bitmap
import android.widget.ImageView
import com.hooitis.hoo.edgecoloringbook.utils.DRAWING_MODE


@Suppress("unused")
class ReviseDrawingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private lateinit var bitmap:Bitmap
    private lateinit var brush: Bitmap
    private lateinit var pencil: Bitmap
    private lateinit var originPencil: Bitmap
    private var mode = 0
    private var drawingMode = DRAWING_MODE
    private val mPath: Path = Path()
    private val mEraserPath: Path = Path()
    private var color: Int = 0
    private var initialize = false

    private var mScaleFactor = 1f

    val TOUCH_TOLERANCE = 4

    private var eraser: Paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        alpha = 0
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20f
        isAntiAlias = true
    }

    private var mPaint: Paint = Paint().apply {
        color = Color.BLACK
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeWidth = 7f
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
        canvas!!.drawBitmap(bitmap, 0f, 0f, null)

        this.canvas.drawPath(mPath, mPaint)
        this.canvas.drawPath(mEraserPath, eraser)
    }

    private fun clear(){
        if(::bitmap.isInitialized)
            bitmap.recycle()

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        invalidate()
    }


    private fun touchStart(x: Float, y: Float){
        if(mode == 0)
            mPath.moveTo(x, y)
        else
            mEraserPath.moveTo(x, y)

        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float){
        val dx = abs(x - mX)
        val dy = abs(y - mY)

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            if(mode == 0)
                mPath.quadTo(mX,mY, (x + mX) / 2, (y + mY)/ 2)
            else
                mEraserPath.quadTo(mX,mY, (x + mX) / 2, (y + mY)/ 2)

            mX = x
            mY = y
        }
    }

    private fun drawPaint(){
        mPath.reset()
    }

    private fun touchUp(){
        mPath.lineTo(mX,mY)
        mEraserPath.lineTo(mX,mY)
        mPath.reset()
        mEraserPath.reset()
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

    fun changeColor(color: Int){
        mPaint.color = color
    }

    fun changeToErase(){
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mode = 1
    }

    fun changeToPencil(){
        mode = 0
    }

    fun setDrawingMode(drawingMode: Int){
        this.drawingMode = drawingMode
    }

    fun brushScale(scaleFactor: Float){
        val scaleBrush = max(1f, min((scaleFactor), 50f))
        mPaint.strokeWidth = scaleBrush
        eraser.strokeWidth = scaleBrush
    }
}