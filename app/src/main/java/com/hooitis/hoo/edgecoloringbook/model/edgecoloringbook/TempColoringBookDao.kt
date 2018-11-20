package com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
interface TempColoringBookDao{
    @Query("SELECT id FROM tempcoloringbook")
    fun get(): List<Long>

    @Query("SELECT * FROM tempcoloringbook where id=:id")
    fun get(id: Long): TempColoringBook

    @Insert(onConflict = REPLACE)
    fun insert(temp: TempColoringBook)

    @Query("DELETE FROM tempcoloringbook where id=:id")
    fun delete(id: Long)

    @Query("DELETE FROM tempcoloringbook")
    fun deleteAll()
}