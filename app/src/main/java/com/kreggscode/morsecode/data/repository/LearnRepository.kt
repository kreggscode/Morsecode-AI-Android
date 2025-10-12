package com.kreggscode.morsecode.data.repository

import com.kreggscode.morsecode.data.local.dao.LearnProgressDao
import com.kreggscode.morsecode.data.local.entities.LearnProgressEntity
import kotlinx.coroutines.flow.Flow

class LearnRepository(private val learnProgressDao: LearnProgressDao) {
    
    fun getAllProgress(): Flow<List<LearnProgressEntity>> {
        return learnProgressDao.getAllProgress()
    }
    
    fun getLearnedCharactersCount(): Flow<Int> {
        return learnProgressDao.getLearnedCharactersCount()
    }
    
    suspend fun getProgressForCharacter(character: String): LearnProgressEntity? {
        return learnProgressDao.getProgressForCharacter(character)
    }
    
    suspend fun updateProgress(character: String, learned: Boolean = true, quizCorrect: Boolean? = null) {
        val existing = learnProgressDao.getProgressForCharacter(character)
        
        if (existing != null) {
            val updated = existing.copy(
                timesLearned = if (learned) existing.timesLearned + 1 else existing.timesLearned,
                lastLearnedTimestamp = System.currentTimeMillis(),
                quizScore = if (quizCorrect == true) existing.quizScore + 1 
                           else if (quizCorrect == false) existing.quizScore 
                           else existing.quizScore,
                totalQuizAttempts = if (quizCorrect != null) existing.totalQuizAttempts + 1 
                                   else existing.totalQuizAttempts
            )
            learnProgressDao.updateProgress(updated)
        } else {
            learnProgressDao.insertProgress(
                LearnProgressEntity(
                    character = character,
                    timesLearned = if (learned) 1 else 0,
                    quizScore = if (quizCorrect == true) 1 else 0,
                    totalQuizAttempts = if (quizCorrect != null) 1 else 0
                )
            )
        }
    }
    
    suspend fun resetProgress() {
        learnProgressDao.deleteAllProgress()
    }
}
