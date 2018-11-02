package com.hooitis.hoo.edgecoloringbook

import android.app.Activity
import android.app.Application
import com.hooitis.hoo.edgecoloringbook.di.AppModule
import com.hooitis.hoo.edgecoloringbook.di.DaggerAppComponent
import com.hooitis.hoo.edgecoloringbook.di.module.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class EdgeColoringBookApp: Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .networkModule(NetworkModule)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

}
