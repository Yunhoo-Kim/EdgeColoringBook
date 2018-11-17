package com.hooitis.hoo.edgecoloringbook.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.databinding.ActivitySplashBinding
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.vm.VersionVM
import javax.inject.Inject


class SplashActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val SPLASH_DELAY: Long = 500
    private val mDelayHandler: Handler by lazy {
        Handler()
    }
    private lateinit var viewModel: VersionVM
    private lateinit var binding: ActivitySplashBinding



    private val mRunnable: Runnable = Runnable {
        if(!isFinishing){
            val intent = Intent(applicationContext, StartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VersionVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        viewModel.checkVersion()
                .subscribe({serverVersion ->
                    val localVersion = viewModel.versionsRepository.loadLocalVersions()
                    if(serverVersion.dbVersion != localVersion.dbVersion){
                        viewModel.versionsRepository.coloringBookRepository.loadColoringBookFromStore().subscribe {
                            mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY)
                        }
                    }
                    else
                        mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY)
                    if(serverVersion.appVersion != localVersion.appVersion){
                        // update application from store
                    }
                    viewModel.versionsRepository.versionsDao.deleteAll()
                    viewModel.versionsRepository.versionsDao.insert(serverVersion)
                }, {

                })

    }
}