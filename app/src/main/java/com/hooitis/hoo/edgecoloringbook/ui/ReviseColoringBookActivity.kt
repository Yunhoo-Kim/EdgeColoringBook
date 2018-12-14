package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min
import android.view.GestureDetector
import android.widget.SeekBar
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityReviseBinding
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBook
import com.hooitis.hoo.edgecoloringbook.utils.*


class ReviseColoringBookActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityReviseBinding
    private lateinit var mScaleDetector: ScaleGestureDetector
    private lateinit var mGestureDetector: GestureDetector
    private var mScaleFactor = 1f
    private var mBScaleFactor = 0.9f
    private var mPosX = 0f
    private var mPosY = 0f
    private lateinit var mBitmap: Bitmap
    private var isFabOpen = false
    private var isModeFabOpen = false
    private var isLineWidthFabOpen = false
    private val backButtonSubject: Subject<Long> = BehaviorSubject.createDefault(0L)
    private var limitWidth: Int = 0
    private var limitHeight: Int = 0

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
        try {
            mBitmap = UiUtils.convertStringToBitmap(passBitmap.imageData)
        }catch (e: NullPointerException){
            finish()
        }

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
                mBScaleFactor = mScaleFactor
                mScaleFactor *= detector!!.scaleFactor
                mScaleFactor = max(MIN_SCALE, min(mScaleFactor, MAX_SCALE))

                viewModel.scaleFactor.value = mScaleFactor
                if(mScaleFactor <= 1.3f){
                    if(!viewModel.saveButtonVisibility.value!!)
                        viewModel.saveButtonVisibility.postValue(true)
                } else{
                    if(viewModel.saveButtonVisibility.value!!)
                        viewModel.saveButtonVisibility.postValue(false)
                }

                if(mScaleFactor > MIN_SCALE && mScaleFactor < 0.9f){
                    binding.drawCont.x = 0.0f
                    binding.drawCont.y = ((binding.containerRevise.height / 2) - (binding.drawCont.height / 2)).toFloat()
                }

                if((binding.drawCont.x > (binding.containerRevise.width + limitWidth) || binding.drawCont.x < -limitWidth) && mBScaleFactor > mScaleFactor){
                    binding.drawCont.x = 0.0f
                    binding.drawCont.y = ((binding.containerRevise.height / 2) - (binding.drawCont.height / 2)).toFloat()
                }
                if((binding.drawCont.y > (binding.containerRevise.height + limitHeight) || binding.drawCont.y < -limitHeight) && mBScaleFactor > mScaleFactor){
                    binding.drawCont.x = 0.0f
                    binding.drawCont.y = ((binding.containerRevise.height / 2) - (binding.drawCont.height / 2)).toFloat()
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
        viewModel.scaleFactor.postValue(0.8f)
        limitWidth = (binding.containerRevise.width / 2)
        limitHeight = (binding.containerRevise.height / 2)
    }


    private fun allowX(deltaX: Float) {
        val futureX = (binding.drawCont.x) - deltaX
        val delta = 0 + (binding.drawCont.width * viewModel.scaleFactor.value!! - binding.drawCont.width) / 1.2
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
        val delta = (binding.drawCont.height * viewModel.scaleFactor.value!! - binding.drawCont.height) / 1.2
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

    private fun openDialog(){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.do_you_want_to_cancel)

        dialog.setPositiveButton(R.string.confirm) { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        dialog.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialog.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        openDialog()
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