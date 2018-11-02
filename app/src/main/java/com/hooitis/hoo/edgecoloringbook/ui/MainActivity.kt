package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AnimationUtils
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.InterstitialAd
//import com.google.android.gms.ads.MobileAds
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityMainBinding
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.utils.AdCount
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import java.lang.Exception
import java.util.*
import javax.inject.Inject


class MainActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityMainBinding
//    private lateinit var mInterstitialAd:InterstitialAd
    private var listening: Boolean = false

    val MIC = 1234

    private val DELAY: Long = 1500
    private val mDelayHandler: Handler by lazy {
        Handler()
    }
//    private val mAdCount: AdCount by lazy {
//        AdCount.getInstance()
//    }

//    private val mShowAdView: Runnable = Runnable {
//        mAdCount.addCount()
//        if(!isFinishing && (mAdCount.count % 2 == 0)){
//            if(mInterstitialAd.isLoaded)
//                mInterstitialAd.show()
//            else
//                finish()
//        }else
//            finish()
//    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

//        MobileAds.initialize(this, getString(R.string.admob))
//        mInterstitialAd = InterstitialAd(this).apply {
//            adUnitId = getString(R.string.admob_id)
//            loadAd(AdRequest.Builder().build())
//            adListener = object : AdListener(){
//                override fun onAdClosed() {
//                    super.onAdClosed()
//                    finish()
//                }
//            }
//        }

        viewModel.wrong.observe(this, android.arch.lifecycle.Observer {
            if(it!!){
                binding.voice.isClickable = false
                viewModel.countDown.postValue(getString(R.string.wrong))
//                mDelayHandler.postDelayed(mShowAdView, DELAY)
            }else{
//                mDelayHandler.removeCallbacks(mShowAdView)
            }
        })
        viewModel.quizIndex.observe(this, android.arch.lifecycle.Observer {
            if (!viewModel.wrong.value!!) {
//                stopListeningSpeech()
            }
        })


        binding.quizList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

            addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
                override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean { return true }
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {} })

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(binding.quizList)
            isLayoutFrozen = true
        }


//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
//            requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), MIC)
//        else{
//        }
        viewModel.loadQuizData()
    }

}