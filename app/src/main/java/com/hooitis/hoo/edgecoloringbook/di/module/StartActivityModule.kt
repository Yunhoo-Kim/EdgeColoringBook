package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.ActivityScope
import com.hooitis.hoo.edgecoloringbook.ui.StartActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class StartActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideQuizStartActivity(activity: StartActivity): StartActivity
}