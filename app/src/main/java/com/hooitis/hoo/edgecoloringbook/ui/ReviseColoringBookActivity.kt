package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityStartupBinding
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min
import android.util.Log
import android.view.GestureDetector
import android.widget.SeekBar
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityReviseBinding
import com.hooitis.hoo.edgecoloringbook.utils.DRAWING_MODE
import com.hooitis.hoo.edgecoloringbook.utils.EdgeDetection
import com.hooitis.hoo.edgecoloringbook.utils.TOUCH_MODE
import java.io.ByteArrayOutputStream


class ReviseColoringBookActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityReviseBinding
    private lateinit var mScaleDetector: ScaleGestureDetector
    private lateinit var mGestureDetector: GestureDetector
    private var mScaleFactor = 1f
    private var mPosX = 0f
    private var mPosY = 0f
    private lateinit var mBitmap: Bitmap

    private val CAMERA = 1231

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_revise)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        val bytes = intent.getByteArrayExtra("bitmap")
        mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        binding.paintView.isDrawingCacheEnabled = true

        binding.saveDrawing.setOnClickListener {
            val stream = ByteArrayOutputStream()
            val resultBitmap = binding.paintView.drawingCache!!
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val intent = Intent(applicationContext, DrawColoringBookActivity::class.java)
            intent.putExtra("bitmap", stream.toByteArray())
            startActivity(intent)
            finish()
        }

        binding.brushScale.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.paintView.brushScale(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding.changeDrawingMode.setOnClickListener {
            if(viewModel.drawingMode.value == DRAWING_MODE){
                viewModel.drawingMode.value = TOUCH_MODE
            }else {
                viewModel.drawingMode.value = DRAWING_MODE
            }
        }

        binding.blackBrush.setOnClickListener {
            viewModel.brushType.value = 0
            binding.paintView.changeColor(Color.BLACK)
        }

        binding.whiteBrush.setOnClickListener {
            viewModel.brushType.value = 0
            binding.paintView.changeColor(Color.WHITE)
        }

        binding.eraserBrush.setOnClickListener {
//            binding.resultImage.setImageBitmap(binding.paintView.drawingCache)
            viewModel.brushType.value = 1
        }

        mScaleDetector = ScaleGestureDetector(this, object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                mScaleFactor *= detector!!.scaleFactor
                mScaleFactor = max(0.5f, min(mScaleFactor, 5f))

                viewModel.scaleFactor.value = mScaleFactor
                return true
            }
        })

        mGestureDetector = GestureDetector(this, object: GestureDetector.SimpleOnGestureListener(){
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                allowX(distanceX)
                allowY(distanceY)
                mPosX = binding.root.x
                mPosY = binding.root.y
                return true
            }
        })

//        binding.resultImage.setImageBitmap(mBitmap)
        binding.paintView.setImageBitmap(mBitmap)

        viewModel.brushColor.observe(this, android.arch.lifecycle.Observer { binding.paintView.changeColor(it!!) })
//        viewModel.scaleFactor.observe(this, android.arch.lifecycle.Observer { binding.paintView.brushScale(it!!) })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mScaleDetector.onTouchEvent(event)
        mGestureDetector.onTouchEvent(event)
        return true
    }

    private fun allowX(deltaX: Float) {
        val futureX = (binding.drawCont.x) - deltaX
        val delta = (binding.drawCont.width * viewModel.scaleFactor.value!! - binding.drawCont.width) / 2
        val limit = binding.root.height - binding.drawCont.height - delta
        var max = delta
        var min = limit

        if(delta < limit){
            max = limit
            min = delta
        }

        if (futureX in min..max) {
            binding.drawCont.x = futureX
        }
    }

    private fun allowY(deltaY: Float) {
        val futureY = (binding.drawCont.y) - deltaY
        val delta = (binding.drawCont.height * viewModel.scaleFactor.value!! - binding.drawCont.height) / 2
        val limit = binding.root.height - binding.drawCont.height - delta
        var max = delta
        var min = limit

        if(delta < limit){
            max = limit
            min = delta
        }

        if (futureY in min..max) {
            binding.drawCont.y = futureY
        }
    }

}