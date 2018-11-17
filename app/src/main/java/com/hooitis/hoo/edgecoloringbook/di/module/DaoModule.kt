package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.model.database.AppDatabase
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.VersionsDao
import com.hooitis.hoo.edgecoloringbook.model.coloringbook.ColoringBookDao
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBook
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBookDao
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.TempColoringBookDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
@Suppress("unused")
class DaoModule {
    @Provides
    @Singleton
    fun provideVersionsDao(database: AppDatabase): VersionsDao = database.versionsDao()

    @Provides
    @Singleton
    fun provideColoringBookDao(database: AppDatabase): ColoringBookDao = database.coloringBookDao()

    @Provides
    @Singleton
    fun provideTempColoringBookDao(database: AppDatabase): TempColoringBookDao = database.tempColoringBookDao()

    @Provides
    @Singleton
    fun providePassColoringBookDao(database: AppDatabase): PassColoringBookDao = database.passColoringBookDao()
}
