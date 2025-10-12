package com.kreggscode.morsecode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.morsecode.data.local.database.MorseDatabase
import com.kreggscode.morsecode.data.local.entities.TranslationEntity
import com.kreggscode.morsecode.data.repository.MorseRepository
import com.kreggscode.morsecode.domain.FlashlightController
import com.kreggscode.morsecode.domain.MorseAudioPlayer
import com.kreggscode.morsecode.domain.MorseTranslator
import com.kreggscode.morsecode.util.SpeechRecognitionHelper
import com.kreggscode.morsecode.util.SpeechRecognitionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VoiceViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: MorseRepository
    private val speechHelper = SpeechRecognitionHelper(application)
    private val translator = MorseTranslator()
    private val audioPlayer = MorseAudioPlayer()
    private val flashlightController = FlashlightController(application)
    
    init {
        val database = MorseDatabase.getDatabase(application)
        repository = MorseRepository(database.translationDao())
    }
    
    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()
    
    fun startListening() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isListening = true,
                recognitionState = "Ready to listen..."
            )
            
            speechHelper.startListening().collect { result ->
                when (result) {
                    is SpeechRecognitionResult.Ready -> {
                        _uiState.value = _uiState.value.copy(
                            recognitionState = "Ready. Start speaking..."
                        )
                    }
                    is SpeechRecognitionResult.Speaking -> {
                        _uiState.value = _uiState.value.copy(
                            recognitionState = "Listening..."
                        )
                    }
                    is SpeechRecognitionResult.RmsChanged -> {
                        _uiState.value = _uiState.value.copy(
                            audioLevel = result.rmsdB
                        )
                    }
                    is SpeechRecognitionResult.PartialResult -> {
                        _uiState.value = _uiState.value.copy(
                            partialText = result.text
                        )
                    }
                    is SpeechRecognitionResult.Success -> {
                        val morse = translator.textToMorse(result.text)
                        _uiState.value = _uiState.value.copy(
                            recognizedText = result.text,
                            morseCode = morse,
                            isListening = false,
                            recognitionState = "Recognition complete"
                        )
                        saveToHistory(result.text, morse)
                    }
                    is SpeechRecognitionResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isListening = false,
                            recognitionState = "Error: ${result.message}"
                        )
                    }
                    is SpeechRecognitionResult.EndOfSpeech -> {
                        _uiState.value = _uiState.value.copy(
                            recognitionState = "Processing..."
                        )
                    }
                }
            }
        }
    }
    
    fun stopListening() {
        speechHelper.stopListening()
        _uiState.value = _uiState.value.copy(
            isListening = false,
            recognitionState = "Stopped"
        )
    }
    
    fun playMorse() {
        if (_uiState.value.morseCode.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPlaying = true)
            audioPlayer.playMorse(
                morse = _uiState.value.morseCode,
                speedMultiplier = 1f,
                volume = 1f
            )
            _uiState.value = _uiState.value.copy(isPlaying = false)
        }
    }
    
    fun flashMorse() {
        if (_uiState.value.morseCode.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isFlashing = true)
            flashlightController.flashMorse(
                morse = _uiState.value.morseCode,
                speedMultiplier = 1f
            )
            _uiState.value = _uiState.value.copy(isFlashing = false)
        }
    }
    
    private fun saveToHistory(text: String, morse: String) {
        viewModelScope.launch {
            repository.insertTranslation(
                TranslationEntity(
                    originalText = text,
                    morseCode = morse,
                    translationType = "VOICE_TO_MORSE"
                )
            )
        }
    }
    
    fun clearResults() {
        _uiState.value = VoiceUiState()
    }
    
    override fun onCleared() {
        super.onCleared()
        speechHelper.stopListening()
        audioPlayer.release()
        flashlightController.release()
    }
}

data class VoiceUiState(
    val isListening: Boolean = false,
    val recognitionState: String = "Tap to start",
    val audioLevel: Float = 0f,
    val partialText: String = "",
    val recognizedText: String = "",
    val morseCode: String = "",
    val isPlaying: Boolean = false,
    val isFlashing: Boolean = false
)
