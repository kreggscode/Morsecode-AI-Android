package com.kreggscode.morsecode.data.repository

import com.kreggscode.morsecode.data.local.dao.TranslationDao
import com.kreggscode.morsecode.data.local.entities.TranslationEntity
import kotlinx.coroutines.flow.Flow

class MorseRepository(private val translationDao: TranslationDao) {
    
    fun getAllTranslations(): Flow<List<TranslationEntity>> {
        return translationDao.getAllTranslations()
    }
    
    fun getFavoriteTranslations(): Flow<List<TranslationEntity>> {
        return translationDao.getFavoriteTranslations()
    }
    
    fun searchTranslations(query: String): Flow<List<TranslationEntity>> {
        return translationDao.searchTranslations(query)
    }
    
    suspend fun getTranslationById(id: Long): TranslationEntity? {
        return translationDao.getTranslationById(id)
    }
    
    suspend fun insertTranslation(translation: TranslationEntity): Long {
        return translationDao.insertTranslation(translation)
    }
    
    suspend fun updateTranslation(translation: TranslationEntity) {
        translationDao.updateTranslation(translation)
    }
    
    suspend fun deleteTranslation(translation: TranslationEntity) {
        translationDao.deleteTranslation(translation)
    }
    
    suspend fun deleteAllTranslations() {
        translationDao.deleteAllTranslations()
    }
    
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        translationDao.updateFavoriteStatus(id, isFavorite)
    }
}
