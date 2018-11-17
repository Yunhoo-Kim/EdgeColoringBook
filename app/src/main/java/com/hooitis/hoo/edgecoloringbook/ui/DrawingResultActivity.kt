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
import javax.inject.Inject
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityDrawingResultBinding
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBook
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.TempColoringBook
import com.hooitis.hoo.edgecoloringbook.utils.*


class DrawingResultActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityDrawingResultBinding
    private lateinit var mBitmap: Bitmap
    private var tempId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawing_result)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        val passBitmap = viewModel.getPassColoringBook()
        mBitmap = UiUtils.convertStringToBitmap(passBitmap.imageData)
        tempId = intent.getLongExtra("tempId", 0)

        binding.saveToGallery.setOnClickListener {
//            viewModel.savePassColoringBook(PassColoringBook(
//                    id = 0,
//                    imageData = UiUtils.convertBitmapToString(mBitmap)))
            val intent = Intent()
            intent.putExtra("save", true)
            setResult(Activity.RESULT_OK, intent)
        }

        binding.saveToTemp.setOnClickListener {
            viewModel.saveTempColoringBook(TempColoringBook(
                    id = tempId,
                    imageData = UiUtils.convertBitmapToString(mBitmap)))
            val intent = Intent()
            intent.putExtra("save", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.resultDrawing.setImageBitmap(mBitmap)
    }
}