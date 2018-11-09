package com.hooitis.hoo.edgecoloringbook.di

import com.hooitis.hoo.edgecoloringbook.di.module.BeforeQuizActivityModule
import com.hooitis.hoo.edgecoloringbook.di.module.MainActivityModule
import com.hooitis.hoo.edgecoloringbook.di.module.QuizStartActivityModule
import com.hooitis.hoo.edgecoloringbook.di.module.SplashActivityModule
import com.hooitis.hoo.edgecoloringbook.ui.BeforeQuizActivity
import com.hooitis.hoo.edgecoloringbook.ui.MainActivity
import com.hooitis.hoo.edgecoloringbook.ui.StartActivity
import com.hooitis.hoo.edgecoloringbook.ui.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
@Suppress("unused")
abstract class ActivityBuilder{
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivityModule(): MainActivity

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivityModule(): SplashActivity

    @ContributesAndroidInjector(modules = [QuizStartActivityModule::class])
    abstract fun bindQuizStartActivityModule(): StartActivity

    @ContributesAndroidInjector(modules = [BeforeQuizActivityModule::class])
    abstract fun bindBeforeQuizActivityModule(): BeforeQuizActivity
}