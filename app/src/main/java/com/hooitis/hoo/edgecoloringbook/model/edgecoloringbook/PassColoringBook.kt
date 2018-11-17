package com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class PassColoringBook(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val imageData:String
)