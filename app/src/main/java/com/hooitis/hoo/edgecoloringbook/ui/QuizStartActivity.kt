package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityStartupBinding
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.utils.Utils
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


class QuizStartActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityStartupBinding
    private val backButtonSubject: Subject<Long> = BehaviorSubject.createDefault(0L)
    private lateinit var mCurrentViewport: RectF
    private lateinit var mScaleDetector: ScaleGestureDetector
    private lateinit var mGestureDetector: GestureDetector
    private var mScaleFactor = 1f
    private var mPosX = 0f
    private var mPosY = 0f
    private lateinit var mBitmap: Bitmap

    private val CAMERA = 1231
    private val DELAY:Long = 1000

    private val mDelayHandler: Handler by lazy {
        Handler()
    }

    private val mRunnable: Runnable = Runnable {
        if(!isFinishing){
            edgeDetection()
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_startup)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)
        mCurrentViewport = RectF(0f, 0f, binding.resultImage.width.toFloat(), binding.resultImage.height.toFloat())

        binding.startQuiz.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, CAMERA)
        }

        binding.changeBrush.setOnClickListener {
            if(viewModel.brushType.value == 0){
                viewModel.brushType.value = 1
            }else{
                viewModel.brushType.value = 0
            }
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

        viewModel.scaleFactor.observe(this, android.arch.lifecycle.Observer {
            binding.paintView.brushScale(it!!)
        })
        initBackPress()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mScaleDetector.onTouchEvent(event)
        mGestureDetector.onTouchEvent(event)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA -> {
                    mBitmap = UiUtils.convertURIBM(contentResolver, Uri.parse(data!!.dataString))
                    mDelayHandler.postDelayed(mRunnable, DELAY)
//                    edgeDetection()
                }
            }
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
                        UiUtils.makeToast(binding.title, R.string.push_again_back_pressed)
                    }
                }.subscribe()
    }

    override fun onBackPressed() {
        backButtonSubject.onNext(Calendar.getInstance().timeInMillis)
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
            Log.d("DrawLineFutureX", "$futureX")
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
            Log.d("DrawLineFutureY", "$futureY")
            binding.drawCont.y = futureY
        }
    }

    private fun edgeDetection(){
        mBitmap = Utils.edgeDetection(mBitmap)
        binding.resultImage.setImageBitmap(mBitmap)
    }
}