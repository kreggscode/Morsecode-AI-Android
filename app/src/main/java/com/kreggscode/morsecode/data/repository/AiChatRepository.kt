package com.kreggscode.morsecode.data.repository

import com.kreggscode.morsecode.data.local.dao.ChatMessageDao
import com.kreggscode.morsecode.data.local.entities.ChatMessageEntity
import com.kreggscode.morsecode.data.remote.ApiClient
import com.kreggscode.morsecode.data.remote.ChatRequest
import com.kreggscode.morsecode.data.remote.Message
import kotlinx.coroutines.flow.Flow

class AiChatRepository(private val chatMessageDao: ChatMessageDao) {
    
    private val apiService = ApiClient.pollinationsApi
    
    fun getAllMessages(): Flow<List<ChatMessageEntity>> {
        return chatMessageDao.getAllMessages()
    }
    
    suspend fun sendMessage(userMessage: String, conversationHistory: List<Message>): Result<String> {
        return try {
            // Save user message
            chatMessageDao.insertMessage(
                ChatMessageEntity(
                    message = userMessage,
                    isUser = true
                )
            )
            
            // Prepare messages with system prompt
            val messages = mutableListOf(
                Message(
                    role = "system",
                    content = "You are a helpful AI assistant specialized in Morse code, telecommunications history, and cryptography. You can answer questions about Samuel Morse, Morse code usage, encryption techniques, and practical applications. Be concise and informative."
                )
            )
            messages.addAll(conversationHistory)
            messages.add(Message(role = "user", content = userMessage))
            
            // Make API call
            val response = apiService.chatCompletion(
                ChatRequest(
                    model = "openai",
                    messages = messages,
                    temperature = 1.0f
                )
            )
            
            val aiResponse = response.choices.firstOrNull()?.message?.content 
                ?: "Sorry, I couldn't generate a response."
            
            // Save AI response
            chatMessageDao.insertMessage(
                ChatMessageEntity(
                    message = aiResponse,
                    isUser = false
                )
            )
            
            Result.success(aiResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearHistory() {
        chatMessageDao.deleteAllMessages()
    }
}
