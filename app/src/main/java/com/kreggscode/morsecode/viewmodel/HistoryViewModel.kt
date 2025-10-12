package com.kreggscode.morsecode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.morsecode.data.local.database.MorseDatabase
import com.kreggscode.morsecode.data.local.entities.TranslationEntity
import com.kreggscode.morsecode.data.repository.MorseRepository
import com.kreggscode.morsecode.domain.MorseAudioPlayer
import com.kreggscode.morsecode.domain.FlashlightController
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: MorseRepository
    private val audioPlayer = MorseAudioPlayer()
    private val flashlightController = FlashlightController(application)
    
    init {
        val database = MorseDatabase.getDatabase(application)
        repository = MorseRepository(database.translationDao())
    }
    
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    
    val translations: StateFlow<List<TranslationEntity>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                if (_uiState.value.showFavoritesOnly) {
                    repository.getFavoriteTranslations()
                } else {
                    repository.getAllTranslations()
                }
            } else {
                repository.searchTranslations(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun toggleFavoritesFilter() {
        _uiState.value = _uiState.value.copy(
            showFavoritesOnly = !_uiState.value.showFavoritesOnly
        )
        _searchQuery.value = _searchQuery.value // Trigger refresh
    }
    
    fun toggleFavorite(translation: TranslationEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(translation.id, !translation.isFavorite)
        }
    }
    
    fun deleteTranslation(translation: TranslationEntity) {
        viewModelScope.launch {
            repository.deleteTranslation(translation)
        }
    }
    
    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAllTranslations()
        }
    }
    
    fun playTranslation(morse: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(playingId = morse.hashCode().toLong())
            audioPlayer.playMorse(morse, speedMultiplier = 1f, volume = 1f)
            _uiState.value = _uiState.value.copy(playingId = null)
        }
    }
    
    fun flashTranslation(morse: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(flashingId = morse.hashCode().toLong())
            flashlightController.flashMorse(morse, speedMultiplier = 1f)
            _uiState.value = _uiState.value.copy(flashingId = null)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
        flashlightController.release()
    }
}

data class HistoryUiState(
    val showFavoritesOnly: Boolean = false,
    val playingId: Long? = null,
    val flashingId: Long? = null
)
