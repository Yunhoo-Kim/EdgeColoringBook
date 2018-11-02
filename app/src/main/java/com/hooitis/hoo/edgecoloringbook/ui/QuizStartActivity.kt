package com.hooitis.hoo.edgecoloringbook.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityStartupBinding
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.utils.Utils
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*


class QuizStartActivity: BaseActivity(){

    private lateinit var binding: ActivityStartupBinding
    private val backButtonSubject: Subject<Long> = BehaviorSubject.createDefault(0L)

    val MIC = 1234
    private val CAMERA = 1231

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_startup)
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        binding.startQuiz.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, CAMERA)
        }

        initBackPress()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA -> {
                    Log.d("EdgeDetection", "load Image")
                    var bitmap = UiUtils.convertURIBM(contentResolver, Uri.parse(data!!.dataString))
                    Log.d("EdgeDetection", "load Image1")
                    bitmap = Utils.edgeDetection(bitmap)
                    Log.d("EdgeDetection", "load Image2")
                    binding.resultImage.setImageBitmap(bitmap)
//                    val matrix = ColorMatrix()
//                    matrix.setSaturation(0f)
//                    val filter = ColorMatrixColorFilter(matrix)
//                    binding.resultImage.colorFilter = filter
                    Log.d("EdgeDetection", "load Image3")
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

    private fun requestMicPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), MIC)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            MIC -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestMicPermission()
                } else {
                }
            }
        }
    }

    override fun onBackPressed() {
        backButtonSubject.onNext(Calendar.getInstance().timeInMillis)
    }
}