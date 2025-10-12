package com.kreggscode.morsecode.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_scores")
data class GameScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameType: String, // DECODER, SPEED_CHALLENGE, MEMORY_MATCH, SOS_RESCUE
    val score: Int,
    val difficulty: String, // EASY, MEDIUM, HARD
    val timestamp: Long = System.currentTimeMillis(),
    val duration: Long = 0 // in milliseconds
)
