package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.ActivityScope
import com.hooitis.hoo.edgecoloringbook.ui.SelectColoringBookActivity
import com.hooitis.hoo.edgecoloringbook.ui.StartActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class SelectColoringBookActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideSelectColoringBookActivity(activity: SelectColoringBookActivity): SelectColoringBookActivity
}