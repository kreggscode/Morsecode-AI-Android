package com.kreggscode.morsecode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.morsecode.data.local.database.MorseDatabase
import com.kreggscode.morsecode.data.local.entities.ChatMessageEntity
import com.kreggscode.morsecode.data.repository.AiChatRepository
import com.kreggscode.morsecode.data.remote.Message
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AiChatViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: AiChatRepository
    
    init {
        val database = MorseDatabase.getDatabase(application)
        repository = AiChatRepository(database.chatMessageDao())
    }
    
    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState: StateFlow<AiChatUiState> = _uiState.asStateFlow()
    
    val messages: StateFlow<List<ChatMessageEntity>> = repository.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun sendMessage(message: String) {
        if (message.isBlank()) return
        
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        
        viewModelScope.launch {
            val conversationHistory = messages.value.takeLast(10).map { msg ->
                Message(
                    role = if (msg.isUser) "user" else "assistant",
                    content = msg.message
                )
            }
            
            val result = repository.sendMessage(message, conversationHistory)
            
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to send message"
                    )
                }
            )
        }
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AiChatUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
