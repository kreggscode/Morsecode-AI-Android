package com.kreggscode.morsecode.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learn_progress")
data class LearnProgressEntity(
    @PrimaryKey
    val character: String,
    val timesLearned: Int = 0,
    val lastLearnedTimestamp: Long = System.currentTimeMillis(),
    val quizScore: Int = 0,
    val totalQuizAttempts: Int = 0
)
