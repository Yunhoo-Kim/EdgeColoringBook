package com.hooitis.hoo.edgecoloringbook.model.proverb

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
interface VersionsDao{
    @Query("SELECT * FROM versions")
    fun get(): List<Versions>

    @Insert(onConflict = REPLACE)
    fun insert(versions: Versions)

    @Query("DELETE FROM versions")
    fun deleteAll()
}