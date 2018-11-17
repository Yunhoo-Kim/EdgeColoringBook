package com.hooitis.hoo.edgecoloringbook.di

import com.hooitis.hoo.edgecoloringbook.di.module.*
import com.hooitis.hoo.edgecoloringbook.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
@Suppress("unused")
abstract class ActivityBuilder{
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivityModule(): MainActivity

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivityModule(): SplashActivity

    @ContributesAndroidInjector(modules = [StartActivityModule::class])
    abstract fun bindStartActivityModule(): StartActivity

    @ContributesAndroidInjector(modules = [ReviseColoringBookActivityModule::class])
    abstract fun bindReviseColoringBookModule(): ReviseColoringBookActivity

    @ContributesAndroidInjector(modules = [DrawColoringBookActivityModule::class])
    abstract fun bindDrawColoringBookModule(): DrawColoringBookActivity

    @ContributesAndroidInjector(modules = [PaletteFragmentModule::class])
    abstract fun bindPaletteFragmentModule(): PaletteFragment

    @ContributesAndroidInjector(modules = [SelectColoringBookActivityModule::class])
    abstract fun bindSelectColoringBookActivityModule(): SelectColoringBookActivity

    @ContributesAndroidInjector(modules = [SelectTempColoringBookActivityModule::class])
    abstract fun bindSelectTempColoringBookActivityModule(): SelectTempColoringBookActivity

    @ContributesAndroidInjector(modules = [DrawingResultActivityModule::class])
    abstract fun bindDrawingResultActivityModule(): DrawingResultActivity
}