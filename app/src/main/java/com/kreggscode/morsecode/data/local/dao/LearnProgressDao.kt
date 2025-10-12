package com.kreggscode.morsecode.data.local.dao

import androidx.room.*
import com.kreggscode.morsecode.data.local.entities.LearnProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnProgressDao {
    
    @Query("SELECT * FROM learn_progress")
    fun getAllProgress(): Flow<List<LearnProgressEntity>>
    
    @Query("SELECT * FROM learn_progress WHERE character = :character")
    suspend fun getProgressForCharacter(character: String): LearnProgressEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: LearnProgressEntity)
    
    @Update
    suspend fun updateProgress(progress: LearnProgressEntity)
    
    @Query("DELETE FROM learn_progress")
    suspend fun deleteAllProgress()
    
    @Query("SELECT COUNT(*) FROM learn_progress WHERE timesLearned > 0")
    fun getLearnedCharactersCount(): Flow<Int>
}
