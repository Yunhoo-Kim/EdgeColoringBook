package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.model.database.AppDatabase
import com.hooitis.hoo.edgecoloringbook.model.proverb.VersionsDao
import com.hooitis.hoo.edgecoloringbook.model.quiz.QuizDao
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
    fun provideQuizDao(database: AppDatabase): QuizDao = database.quizDao()
}
