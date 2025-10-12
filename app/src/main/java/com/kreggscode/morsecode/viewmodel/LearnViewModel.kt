package com.kreggscode.morsecode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.morsecode.data.local.database.MorseDatabase
import com.kreggscode.morsecode.data.repository.LearnRepository
import com.kreggscode.morsecode.domain.MorseAudioPlayer
import com.kreggscode.morsecode.domain.MorseTranslator
import com.kreggscode.morsecode.util.Constants
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LearnViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: LearnRepository
    private val translator = MorseTranslator()
    private val audioPlayer = MorseAudioPlayer()
    
    init {
        val database = MorseDatabase.getDatabase(application)
        repository = LearnRepository(database.learnProgressDao())
    }
    
    private val _uiState = MutableStateFlow(LearnUiState())
    val uiState: StateFlow<LearnUiState> = _uiState.asStateFlow()
    
    val learnedCount: StateFlow<Int> = repository.getLearnedCharactersCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    init {
        loadCharacters()
    }
    
    private fun loadCharacters() {
        val letters = ('A'..'Z').map { char ->
            MorseCharacter(
                char = char.toString(),
                morse = translator.getCharMorse(char) ?: "",
                category = "Letters"
            )
        }
        
        val numbers = ('0'..'9').map { char ->
            MorseCharacter(
                char = char.toString(),
                morse = translator.getCharMorse(char) ?: "",
                category = "Numbers"
            )
        }
        
        val punctuation = listOf('.', ',', '?', '\'', '!', '/', '(', ')', '&', ':', ';', '=', '+', '-', '_', '"', '$', '@').map { char ->
            MorseCharacter(
                char = char.toString(),
                morse = translator.getCharMorse(char) ?: "",
                category = "Punctuation"
            )
        }
        
        _uiState.value = _uiState.value.copy(
            allCharacters = letters + numbers + punctuation,
            filteredCharacters = letters
        )
    }
    
    fun selectCategory(category: String) {
        val filtered = _uiState.value.allCharacters.filter { it.category == category }
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            filteredCharacters = filtered
        )
    }
    
    fun playCharacter(character: String) {
        val morse = translator.getCharMorse(character.firstOrNull() ?: return) ?: return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(playingCharacter = character)
            audioPlayer.playMorse(morse, speedMultiplier = 0.8f, volume = 1f)
            _uiState.value = _uiState.value.copy(playingCharacter = null)
            
            // Update progress
            repository.updateProgress(character, learned = true)
        }
    }
    
    fun startQuiz() {
        val characters = _uiState.value.filteredCharacters.shuffled()
        if (characters.isEmpty()) return
        
        _uiState.value = _uiState.value.copy(
            isQuizMode = true,
            quizCharacters = characters,
            currentQuizIndex = 0,
            quizScore = 0,
            quizAnswered = false
        )
        
        playCurrentQuizCharacter()
    }
    
    private fun playCurrentQuizCharacter() {
        val current = _uiState.value.quizCharacters.getOrNull(_uiState.value.currentQuizIndex) ?: return
        viewModelScope.launch {
            audioPlayer.playMorse(current.morse, speedMultiplier = 1f, volume = 1f)
        }
    }
    
    fun submitQuizAnswer(answer: String) {
        val current = _uiState.value.quizCharacters.getOrNull(_uiState.value.currentQuizIndex) ?: return
        val isCorrect = answer.equals(current.char, ignoreCase = true)
        
        viewModelScope.launch {
            repository.updateProgress(current.char, learned = false, quizCorrect = isCorrect)
        }
        
        _uiState.value = _uiState.value.copy(
            quizScore = if (isCorrect) _uiState.value.quizScore + 1 else _uiState.value.quizScore,
            quizAnswered = true,
            lastAnswerCorrect = isCorrect
        )
    }
    
    fun nextQuizQuestion() {
        val nextIndex = _uiState.value.currentQuizIndex + 1
        
        if (nextIndex >= _uiState.value.quizCharacters.size) {
            // Quiz complete
            _uiState.value = _uiState.value.copy(
                isQuizMode = false,
                quizComplete = true
            )
        } else {
            _uiState.value = _uiState.value.copy(
                currentQuizIndex = nextIndex,
                quizAnswered = false,
                lastAnswerCorrect = null
            )
            playCurrentQuizCharacter()
        }
    }
    
    fun replayQuizCharacter() {
        playCurrentQuizCharacter()
    }
    
    fun exitQuiz() {
        _uiState.value = _uiState.value.copy(
            isQuizMode = false,
            quizComplete = false
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}

data class LearnUiState(
    val allCharacters: List<MorseCharacter> = emptyList(),
    val filteredCharacters: List<MorseCharacter> = emptyList(),
    val selectedCategory: String = "Letters",
    val playingCharacter: String? = null,
    val isQuizMode: Boolean = false,
    val quizCharacters: List<MorseCharacter> = emptyList(),
    val currentQuizIndex: Int = 0,
    val quizScore: Int = 0,
    val quizAnswered: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val quizComplete: Boolean = false
)

data class MorseCharacter(
    val char: String,
    val morse: String,
    val category: String
)
