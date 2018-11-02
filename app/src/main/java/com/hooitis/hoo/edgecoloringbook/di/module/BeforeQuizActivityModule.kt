package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.ActivityScope
import com.hooitis.hoo.edgecoloringbook.ui.BeforeQuizActivity
import com.hooitis.hoo.edgecoloringbook.ui.MainActivity
import com.hooitis.hoo.edgecoloringbook.ui.QuizStartActivity
import com.hooitis.hoo.edgecoloringbook.ui.SplashActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class BeforeQuizActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideBeforeQuizActivity(activity: BeforeQuizActivity): BeforeQuizActivity
}