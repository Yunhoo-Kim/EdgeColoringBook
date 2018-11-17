package com.hooitis.hoo.edgecoloringbook.model.coloringbook

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
@Suppress("unused")
interface ColoringBookDao{

    @Query("SELECT * FROM coloringbook")
    fun get(): List<ColoringBook>

    @Insert(onConflict = REPLACE)
    fun insert(edgeColoringBook: ColoringBook)

    @Query("DELETE FROM coloringbook")
    fun deleteAll()
}