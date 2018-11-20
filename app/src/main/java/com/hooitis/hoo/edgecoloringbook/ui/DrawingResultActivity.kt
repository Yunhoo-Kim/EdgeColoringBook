package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
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
import java.util.*


class DrawingResultActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityDrawingResultBinding
    private lateinit var mBitmap: Bitmap
    private var tempId: Long = 0
    private lateinit var mInterstitialAd: InterstitialAd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this,  getString(R.string.admob))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawing_result)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        val passBitmap = viewModel.getPassColoringBook()
        mBitmap = UiUtils.convertStringToBitmap(passBitmap.imageData)
        tempId = intent.getLongExtra("tempId", 0)

        binding.saveToGallery.setOnClickListener {
            saveToGallery()
        }

        binding.saveToTemp.setOnClickListener {
            viewModel.saveTempColoringBook(TempColoringBook(
                    id = tempId,
                    imageData = UiUtils.convertBitmapToString(mBitmap)))
            finishActivity()
        }

        binding.resultDrawing.setImageBitmap(mBitmap)
    }

    private fun finishActivity(){
        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.admob_id)
            loadAd(AdRequest.Builder().build())
            adListener = object : AdListener(){
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    show()
                }
                override fun onAdFailedToLoad(p0: Int) {
                    super.onAdFailedToLoad(p0)
                    val intent = Intent()
                    intent.putExtra("save", true)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                override fun onAdClosed() {
                    super.onAdClosed()
                    val intent = Intent()
                    intent.putExtra("save", true)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun saveToGallery(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        else{
            MediaStore.Images.Media.insertImage(contentResolver, mBitmap, "${UUID.randomUUID()}", getString(R.string.app_name))

            if(tempId != 0L)
                viewModel.deleteTempColoringBook(tempId)

            finishActivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            0 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    saveToGallery()
                } else {
                    saveToGallery()
                }
            }
        }
    }
}