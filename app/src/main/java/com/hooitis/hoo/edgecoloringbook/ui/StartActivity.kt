package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.ScaleGestureDetector
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*
import javax.inject.Inject
import android.view.GestureDetector
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityStartBinding
import com.hooitis.hoo.edgecoloringbook.utils.DRAWING_MODE
import com.hooitis.hoo.edgecoloringbook.utils.EdgeDetection
import com.hooitis.hoo.edgecoloringbook.utils.TOUCH_MODE
import java.io.ByteArrayOutputStream


class StartActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityStartBinding
    private val backButtonSubject: Subject<Long> = BehaviorSubject.createDefault(0L)
    private lateinit var mBitmap: Bitmap

    private val CAMERA = 1231

    private val mRunnable: Runnable = Runnable {
        if(!isFinishing){
            edgeDetection()
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        binding.makeColoringBook.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, CAMERA)
        }

        viewModel.processingImage.observe(this, android.arch.lifecycle.Observer {
            when(it!!){
                2 -> {
                    val stream = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val intent = Intent(applicationContext, ReviseColoringBookActivity::class.java)
                    intent.putExtra("bitmap", stream.toByteArray())
                    startActivity(intent)
                }
                else -> { }
            }
        })

        binding.selectColoringBook.setOnClickListener {}
        initBackPress()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA -> {
                    mBitmap = UiUtils.convertURIBM(contentResolver, Uri.parse(data!!.dataString))
                    Thread(mRunnable).start()
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
                        UiUtils.makeToast(binding.root, R.string.push_again_back_pressed)
                    }
                }.subscribe()
    }

    override fun onBackPressed() {
        backButtonSubject.onNext(Calendar.getInstance().timeInMillis)
    }

    private fun edgeDetection(){
        val mEdgeDetection = EdgeDetection()
        mBitmap = mEdgeDetection.edgeDetection(mBitmap)
        viewModel.processingImage.postValue(2)
    }
}