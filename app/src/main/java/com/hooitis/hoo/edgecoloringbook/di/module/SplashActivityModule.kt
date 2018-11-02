package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.ActivityScope
import com.hooitis.hoo.edgecoloringbook.ui.MainActivity
import com.hooitis.hoo.edgecoloringbook.ui.SplashActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class SplashActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideSplashActivity(activity: SplashActivity): SplashActivity
}