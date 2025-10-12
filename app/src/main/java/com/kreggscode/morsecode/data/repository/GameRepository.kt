package com.kreggscode.morsecode.data.repository

import com.kreggscode.morsecode.data.local.dao.GameScoreDao
import com.kreggscode.morsecode.data.local.entities.GameScoreEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameScoreDao: GameScoreDao) {
    
    fun getAllScores(): Flow<List<GameScoreEntity>> {
        return gameScoreDao.getAllScores()
    }
    
    fun getTopScoresForGame(gameType: String): Flow<List<GameScoreEntity>> {
        return gameScoreDao.getTopScoresForGame(gameType)
    }
    
    suspend fun getHighScore(gameType: String, difficulty: String): Int {
        return gameScoreDao.getHighScore(gameType, difficulty) ?: 0
    }
    
    suspend fun saveScore(
        gameType: String,
        score: Int,
        difficulty: String,
        duration: Long
    ): Long {
        return gameScoreDao.insertScore(
            GameScoreEntity(
                gameType = gameType,
                score = score,
                difficulty = difficulty,
                duration = duration
            )
        )
    }
    
    suspend fun deleteAllScores() {
        gameScoreDao.deleteAllScores()
    }
}
