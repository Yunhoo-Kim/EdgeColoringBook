package com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
interface PassColoringBookDao{
    @Query("SELECT * FROM passcoloringbook")
    fun get(): List<PassColoringBook>

    @Query("SELECT * FROM passcoloringbook where id=:id")
    fun get(id: Long): PassColoringBook

    @Insert(onConflict = REPLACE)
    fun insert(pass: PassColoringBook)

    @Query("DELETE FROM passcoloringbook")
    fun deleteAll()
}