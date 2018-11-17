package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.*
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityReviseBinding
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBook
import com.hooitis.hoo.edgecoloringbook.utils.*
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
    private var isFabOpen = false
    private var isModeFabOpen = false
    private var isLineWidthFabOpen = false
    private val backButtonSubject: Subject<Long> = BehaviorSubject.createDefault(0L)

    private val CAMERA = 1231

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_revise)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        val passBitmap = viewModel.getPassColoringBook()
        mBitmap = UiUtils.convertStringToBitmap(passBitmap.imageData)

        binding.saveDrawing.setOnClickListener {
            val resultBitmap = Bitmap.createBitmap(
                    binding.paintView.width,
                    binding.paintView.height,
                    Bitmap.Config.ARGB_8888)

            val canvas = Canvas(resultBitmap)
            binding.paintView.draw(canvas)

            viewModel.savePassColoringBook(PassColoringBook(
                    id = 0,
                    imageData = UiUtils.convertBitmapToString(resultBitmap)))
            val intent = Intent(applicationContext, DrawColoringBookActivity::class.java)
            startActivityForResult(intent, 0)
        }


        binding.blackBrush.setOnClickListener {
            viewModel.brushType.value = 0
            viewModel.brushColor.value = Color.BLACK
            viewModel.drawingMode.value = DRAWING_MODE
            binding.paintView.changeColor(Color.BLACK)
        }

        binding.whiteBrush.setOnClickListener {
            viewModel.brushType.value = 0
            viewModel.brushColor.value = Color.WHITE
            viewModel.drawingMode.value = DRAWING_MODE
            binding.paintView.changeColor(Color.WHITE)
        }

        binding.eraserBrush.setOnClickListener {
            viewModel.drawingMode.value = DRAWING_MODE
            viewModel.brushType.value = 1
        }


        binding.modeSetting.setOnClickListener {
            if(viewModel.drawingMode.value == DRAWING_MODE){
                viewModel.drawingMode.value = TOUCH_MODE
            }
            else{
                viewModel.drawingMode.value = DRAWING_MODE
            }
        }

        viewModel.drawingMode.observe(this, android.arch.lifecycle.Observer {
            if(viewModel.drawingMode.value == DRAWING_MODE){
                binding.modeSetting.setImageDrawable(getDrawable(R.drawable.drawing))
            }
            else{
                binding.modeSetting.setImageDrawable(getDrawable(R.drawable.zoom))
            }
        })


        binding.brushSizeBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.paintView.brushScale(progress.toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        mScaleDetector = ScaleGestureDetector(this, object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                mScaleFactor *= detector!!.scaleFactor
                mScaleFactor = max(MIN_SCALE, min(mScaleFactor, MAX_SCALE))

                viewModel.scaleFactor.value = mScaleFactor
                if(mScaleFactor < 1.0f){
                    binding.drawCont.x = 0.0f
                    binding.drawCont.y = 0.0f
                }
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

        viewModel.brushColor.observe(this, android.arch.lifecycle.Observer { binding.paintView.changeColor(it!!) })

        fitToBitmap()

        binding.paintView.isDrawingCacheEnabled = true
        binding.paintView.buildDrawingCache(true)
        binding.paintView.setImageBitmap(mBitmap)

        initBackPress()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mScaleDetector.onTouchEvent(event)
        mGestureDetector.onTouchEvent(event)
        return true
    }

    private fun fitToBitmap(){
        val point = Point()
        windowManager.defaultDisplay.getSize(point)

        val layoutParams = binding.drawCont.layoutParams

        if(mBitmap.height > mBitmap.width){
            val ratio = mBitmap.height.toDouble() / mBitmap.width.toDouble()
            layoutParams.height = (point.x * ratio).toInt()
            layoutParams.width = point.x
        }else{
            val ratio = mBitmap.width.toDouble() / mBitmap.height.toDouble()
            layoutParams.height = (point.x / ratio).toInt()
            layoutParams.width = point.x
        }

        binding.drawCont.layoutParams = layoutParams

    }


    private fun allowX(deltaX: Float) {
        val futureX = (binding.drawCont.x) - deltaX
        val delta = 0 + (binding.drawCont.width * viewModel.scaleFactor.value!! - binding.drawCont.width) / 2
        val limit = binding.root.width - binding.drawCont.width - delta
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

    private fun initBackPress(){
        backButtonSubject.toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(2, 1)
                .map {
                    Pair<Long, Long>(it[0], it[1])
                }
                .doOnNext { t->
                    if (t != null && t.second - t.first < 1000) {
                        super.onBackPressed()
                    } else {
                        UiUtils.makeToast(binding.root, R.string.push_again_back_pressed)
                    }
                }.subscribe()
    }

    override fun onBackPressed() {
        backButtonSubject.onNext(Calendar.getInstance().timeInMillis)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val intent = Intent()
            intent.putExtra("save", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}