package com.hooitis.hoo.edgecoloringbook.model.quiz

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class Quiz(
        @PrimaryKey(autoGenerate = false)
        val id: Long = 0,
        val proverb: String,
        val questionProverb: String,
        val answerProverb: String,
        val meaning: String
)