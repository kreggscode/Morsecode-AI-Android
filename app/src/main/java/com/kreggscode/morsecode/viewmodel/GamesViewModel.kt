package com.kreggscode.morsecode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.morsecode.data.local.database.MorseDatabase
import com.kreggscode.morsecode.data.repository.GameRepository
import com.kreggscode.morsecode.domain.MorseAudioPlayer
import com.kreggscode.morsecode.domain.MorseTranslator
import com.kreggscode.morsecode.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GamesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: GameRepository
    private val translator = MorseTranslator()
    private val audioPlayer = MorseAudioPlayer()
    
    init {
        val database = MorseDatabase.getDatabase(application)
        repository = GameRepository(database.gameScoreDao())
    }
    
    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()
    
    fun selectGame(gameType: GameType) {
        _uiState.value = _uiState.value.copy(selectedGame = gameType)
    }
    
    fun startGame(difficulty: String) {
        when (_uiState.value.selectedGame) {
            GameType.DECODER -> startDecoderGame(difficulty)
            GameType.SPEED_CHALLENGE -> startSpeedGame(difficulty)
            GameType.MEMORY_MATCH -> startMemoryGame(difficulty)
            GameType.SOS_RESCUE -> startSOSGame(difficulty)
            null -> {}
        }
    }
    
    private fun startDecoderGame(difficulty: String) {
        val characters = ('A'..'Z').toList() + ('0'..'9').toList()
        val targetChar = characters.random()
        val morse = translator.getCharMorse(targetChar) ?: return
        
        _uiState.value = _uiState.value.copy(
            isPlaying = true,
            difficulty = difficulty,
            currentQuestion = morse,
            correctAnswer = targetChar.toString(),
            score = 0,
            questionNumber = 1,
            totalQuestions = 10,
            gameStartTime = System.currentTimeMillis()
        )
        
        playMorse(morse)
    }
    
    private fun startSpeedGame(difficulty: String) {
        startDecoderGame(difficulty)
    }
    
    private fun startMemoryGame(difficulty: String) {
        val pairs = mutableListOf<MemoryCard>()
        val characters = ('A'..'H').toList()
        
        characters.forEach { char ->
            val morse = translator.getCharMorse(char) ?: return
            pairs.add(MemoryCard(char.toString(), morse, false))
            pairs.add(MemoryCard(char.toString(), morse, false))
        }
        
        _uiState.value = _uiState.value.copy(
            isPlaying = true,
            difficulty = difficulty,
            memoryCards = pairs.shuffled(),
            score = 0,
            gameStartTime = System.currentTimeMillis()
        )
    }
    
    private fun startSOSGame(difficulty: String) {
        startDecoderGame(difficulty)
    }
    
    fun submitAnswer(answer: String) {
        val isCorrect = answer.equals(_uiState.value.correctAnswer, ignoreCase = true)
        val points = if (isCorrect) Constants.GAME_POINTS_CORRECT else Constants.GAME_POINTS_WRONG
        
        _uiState.value = _uiState.value.copy(
            score = (_uiState.value.score + points).coerceAtLeast(0),
            lastAnswerCorrect = isCorrect
        )
        
        if (_uiState.value.questionNumber >= _uiState.value.totalQuestions) {
            endGame()
        } else {
            nextQuestion()
        }
    }
    
    private fun nextQuestion() {
        val characters = ('A'..'Z').toList() + ('0'..'9').toList()
        val targetChar = characters.random()
        val morse = translator.getCharMorse(targetChar) ?: return
        
        _uiState.value = _uiState.value.copy(
            questionNumber = _uiState.value.questionNumber + 1,
            currentQuestion = morse,
            correctAnswer = targetChar.toString(),
            lastAnswerCorrect = null
        )
        
        playMorse(morse)
    }
    
    private fun playMorse(morse: String) {
        viewModelScope.launch {
            audioPlayer.playMorse(morse, speedMultiplier = 1f, volume = 1f)
        }
    }
    
    fun replayQuestion() {
        playMorse(_uiState.value.currentQuestion)
    }
    
    private fun endGame() {
        val duration = System.currentTimeMillis() - _uiState.value.gameStartTime
        
        viewModelScope.launch {
            _uiState.value.selectedGame?.let { gameType ->
                repository.saveScore(
                    gameType = gameType.name,
                    score = _uiState.value.score,
                    difficulty = _uiState.value.difficulty,
                    duration = duration
                )
            }
        }
        
        _uiState.value = _uiState.value.copy(
            isPlaying = false,
            gameComplete = true
        )
    }
    
    fun exitGame() {
        _uiState.value = GamesUiState()
    }
    
    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}

data class GamesUiState(
    val selectedGame: GameType? = null,
    val isPlaying: Boolean = false,
    val difficulty: String = "EASY",
    val score: Int = 0,
    val currentQuestion: String = "",
    val correctAnswer: String = "",
    val questionNumber: Int = 0,
    val totalQuestions: Int = 10,
    val lastAnswerCorrect: Boolean? = null,
    val gameComplete: Boolean = false,
    val gameStartTime: Long = 0,
    val memoryCards: List<MemoryCard> = emptyList()
)

enum class GameType {
    DECODER,
    SPEED_CHALLENGE,
    MEMORY_MATCH,
    SOS_RESCUE
}

data class MemoryCard(
    val value: String,
    val morse: String,
    val isFlipped: Boolean
)
