package com.kreggscode.morsecode.data.local.dao

import androidx.room.*
import com.kreggscode.morsecode.data.local.entities.GameScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameScoreDao {
    
    @Query("SELECT * FROM game_scores ORDER BY timestamp DESC")
    fun getAllScores(): Flow<List<GameScoreEntity>>
    
    @Query("SELECT * FROM game_scores WHERE gameType = :gameType ORDER BY score DESC LIMIT 10")
    fun getTopScoresForGame(gameType: String): Flow<List<GameScoreEntity>>
    
    @Query("SELECT MAX(score) FROM game_scores WHERE gameType = :gameType AND difficulty = :difficulty")
    suspend fun getHighScore(gameType: String, difficulty: String): Int?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: GameScoreEntity): Long
    
    @Delete
    suspend fun deleteScore(score: GameScoreEntity)
    
    @Query("DELETE FROM game_scores")
    suspend fun deleteAllScores()
}
