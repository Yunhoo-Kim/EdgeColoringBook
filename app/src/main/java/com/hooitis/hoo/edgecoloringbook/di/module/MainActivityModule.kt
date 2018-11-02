package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.ActivityScope
import com.hooitis.hoo.edgecoloringbook.ui.MainActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class MainActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideMainActivity(activity: MainActivity): MainActivity
}