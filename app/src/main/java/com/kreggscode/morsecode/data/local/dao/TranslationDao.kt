package com.kreggscode.morsecode.data.local.dao

import androidx.room.*
import com.kreggscode.morsecode.data.local.entities.TranslationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    
    @Query("SELECT * FROM translations ORDER BY timestamp DESC")
    fun getAllTranslations(): Flow<List<TranslationEntity>>
    
    @Query("SELECT * FROM translations WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteTranslations(): Flow<List<TranslationEntity>>
    
    @Query("SELECT * FROM translations WHERE originalText LIKE '%' || :query || '%' OR morseCode LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchTranslations(query: String): Flow<List<TranslationEntity>>
    
    @Query("SELECT * FROM translations WHERE id = :id")
    suspend fun getTranslationById(id: Long): TranslationEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity): Long
    
    @Update
    suspend fun updateTranslation(translation: TranslationEntity)
    
    @Delete
    suspend fun deleteTranslation(translation: TranslationEntity)
    
    @Query("DELETE FROM translations")
    suspend fun deleteAllTranslations()
    
    @Query("UPDATE translations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)
}
