package com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class TempColoringBook(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val imageData:String
)