package com.kreggscode.morsecode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.morsecode.data.local.database.MorseDatabase
import com.kreggscode.morsecode.data.local.entities.TranslationEntity
import com.kreggscode.morsecode.data.repository.MorseRepository
import com.kreggscode.morsecode.domain.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: MorseRepository
    private val translator = MorseTranslator()
    private val audioPlayer = MorseAudioPlayer()
    private val flashlightController = FlashlightController(application)
    private val encryptionManager = EncryptionManager()
    
    init {
        val database = MorseDatabase.getDatabase(application)
        repository = MorseRepository(database.translationDao())
    }
    
    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()
    
    fun onTextInputChanged(text: String) {
        _uiState.value = _uiState.value.copy(
            textInput = text,
            morseOutput = if (_uiState.value.encryptionType == EncryptionType.NONE) {
                translator.textToMorse(text)
            } else {
                encryptionManager.encryptAndMorse(
                    text,
                    _uiState.value.encryptionType,
                    _uiState.value.encryptionKey
                )
            }
        )
    }
    
    fun onMorseInputChanged(morse: String) {
        if (translator.isValidMorse(morse)) {
            _uiState.value = _uiState.value.copy(
                morseInput = morse,
                textOutput = if (_uiState.value.encryptionType == EncryptionType.NONE) {
                    translator.morseToText(morse)
                } else {
                    encryptionManager.morseAndDecrypt(
                        morse,
                        _uiState.value.encryptionType,
                        _uiState.value.encryptionKey
                    )
                }
            )
        }
    }
    
    fun playMorseAudio() {
        val morse = _uiState.value.morseOutput.ifEmpty { _uiState.value.morseInput }
        if (morse.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPlaying = true)
            audioPlayer.playMorse(
                morse = morse,
                speedMultiplier = _uiState.value.playbackSpeed,
                volume = 1f,
                onProgress = { current, total ->
                    _uiState.value = _uiState.value.copy(
                        playbackProgress = current.toFloat() / total
                    )
                }
            )
            _uiState.value = _uiState.value.copy(
                isPlaying = false,
                playbackProgress = 0f
            )
        }
    }
    
    fun stopMorseAudio() {
        audioPlayer.stopPlayback()
        _uiState.value = _uiState.value.copy(
            isPlaying = false,
            playbackProgress = 0f
        )
    }
    
    fun stopPlayback() {
        stopMorseAudio()
    }
    
    fun stopFlashlight() {
        stopFlashing()
    }
    
    fun translateMorseToText(morse: String) {
        _uiState.value = _uiState.value.copy(
            textInput = morse,
            morseOutput = translator.morseToText(morse)
        )
    }
    
    fun flashMorse() {
        val morse = _uiState.value.morseOutput.ifEmpty { _uiState.value.morseInput }
        if (morse.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isFlashing = true)
            flashlightController.flashMorse(
                morse = morse,
                speedMultiplier = _uiState.value.playbackSpeed,
                onProgress = { current, total ->
                    _uiState.value = _uiState.value.copy(
                        flashProgress = current.toFloat() / total
                    )
                }
            )
            _uiState.value = _uiState.value.copy(
                isFlashing = false,
                flashProgress = 0f
            )
        }
    }
    
    fun stopFlashing() {
        flashlightController.stopFlashing()
        _uiState.value = _uiState.value.copy(
            isFlashing = false,
            flashProgress = 0f
        )
    }
    
    fun saveToHistory() {
        viewModelScope.launch {
            val text = _uiState.value.textInput.ifEmpty { _uiState.value.textOutput }
            val morse = _uiState.value.morseOutput.ifEmpty { _uiState.value.morseInput }
            
            if (text.isNotEmpty() && morse.isNotEmpty()) {
                repository.insertTranslation(
                    TranslationEntity(
                        originalText = text,
                        morseCode = morse,
                        translationType = if (_uiState.value.textInput.isNotEmpty()) "TEXT_TO_MORSE" else "MORSE_TO_TEXT",
                        encryptionType = _uiState.value.encryptionType.name,
                        encryptionKey = _uiState.value.encryptionKey?.toString()
                    )
                )
            }
        }
    }
    
    fun setPlaybackSpeed(speed: Float) {
        _uiState.value = _uiState.value.copy(playbackSpeed = speed)
    }
    
    fun setEncryptionType(type: EncryptionType) {
        _uiState.value = _uiState.value.copy(encryptionType = type)
        // Re-translate with new encryption
        if (_uiState.value.textInput.isNotEmpty()) {
            onTextInputChanged(_uiState.value.textInput)
        }
    }
    
    fun setEncryptionKey(key: Any?) {
        _uiState.value = _uiState.value.copy(encryptionKey = key)
        // Re-translate with new key
        if (_uiState.value.textInput.isNotEmpty()) {
            onTextInputChanged(_uiState.value.textInput)
        }
    }
    
    fun clearAll() {
        _uiState.value = TranslatorUiState()
    }
    
    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
        flashlightController.release()
    }
}

data class TranslatorUiState(
    val textInput: String = "",
    val morseInput: String = "",
    val morseOutput: String = "",
    val textOutput: String = "",
    val isPlaying: Boolean = false,
    val isFlashing: Boolean = false,
    val playbackProgress: Float = 0f,
    val flashProgress: Float = 0f,
    val playbackSpeed: Float = 1f,
    val encryptionType: EncryptionType = EncryptionType.NONE,
    val encryptionKey: Any? = null
)
