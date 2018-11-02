package com.hooitis.hoo.edgecoloringbook.model.quiz

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
@Suppress("unused")
interface QuizDao{

    @Query("SELECT * FROM quiz")
    fun get(): List<Quiz>

    @Insert(onConflict = REPLACE)
    fun insert(quiz : Quiz)

    @Query("DELETE FROM quiz")
    fun deleteAll()
}