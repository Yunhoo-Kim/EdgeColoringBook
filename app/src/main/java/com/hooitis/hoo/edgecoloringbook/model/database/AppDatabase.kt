package com.hooitis.hoo.edgecoloringbook.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.hooitis.hoo.edgecoloringbook.model.coloringbook.ColoringBook
import com.hooitis.hoo.edgecoloringbook.model.coloringbook.ColoringBookDao
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.*

@Database(entities = [Versions::class, ColoringBook::class, TempColoringBook::class, PassColoringBook::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun versionsDao(): VersionsDao
    abstract fun coloringBookDao(): ColoringBookDao
    abstract fun tempColoringBookDao(): TempColoringBookDao
    abstract fun passColoringBookDao(): PassColoringBookDao
//    companion object {
//        @JvmField
//        val MIGRATION_1_2 = Migration1To2()
//    }
}
