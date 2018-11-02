package com.hooitis.hoo.edgecoloringbook.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.hooitis.hoo.edgecoloringbook.model.proverb.Versions
import com.hooitis.hoo.edgecoloringbook.model.proverb.VersionsDao
import com.hooitis.hoo.edgecoloringbook.model.quiz.Quiz
import com.hooitis.hoo.edgecoloringbook.model.quiz.QuizDao

@Database(entities = [Versions::class, Quiz::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun versionsDao(): VersionsDao
    abstract fun quizDao(): QuizDao
//    abstract fun userDao(): UserDao
//    abstract fun foodDao(): FoodDao
//    abstract fun dietDao(): DietDao
}
