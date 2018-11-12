package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.ActivityScope
import com.hooitis.hoo.edgecoloringbook.ui.ReviseColoringBookActivity
import com.hooitis.hoo.edgecoloringbook.ui.StartActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class ReviseColoringBookActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideReviseColoringBookActivity(activity: ReviseColoringBookActivity): ReviseColoringBookActivity
}