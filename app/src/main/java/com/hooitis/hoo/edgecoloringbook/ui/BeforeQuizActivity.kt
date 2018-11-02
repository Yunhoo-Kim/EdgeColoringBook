package com.hooitis.hoo.edgecoloringbook.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.databinding.ActivityBeforeBinding
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.vm.VersionVM
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class BeforeQuizActivity: BaseActivity(){

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: VersionVM
    private lateinit var binding: ActivityBeforeBinding

    private var CNT: Int = 4
    private val DELAY: Long = 3100
    private val mDelayHandler: Handler by lazy {
        Handler()
    }

    private val mRunnable: Runnable = Runnable {
        if(!isFinishing){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_before)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VersionVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)
        mDelayHandler.postDelayed(mRunnable, DELAY)

        val timer = Timer("Count", false)
        timer.schedule(0, 1000){
            CNT--
            if(CNT == 0){
                cancel()
            }else {
                viewModel.beforeQuizText.postValue("$CNT${getString(R.string.quiz_start_after_second)}")
            }
        }
    }
}