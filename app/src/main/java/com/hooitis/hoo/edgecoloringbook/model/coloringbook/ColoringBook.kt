package com.hooitis.hoo.edgecoloringbook.model.coloringbook

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class ColoringBook(
        @PrimaryKey(autoGenerate = false)
        val id: Long = 0,
        val url: String
)