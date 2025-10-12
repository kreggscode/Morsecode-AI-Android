package com.kreggscode.morsecode.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface PollinationsApiService {
    
    @POST("openai")
    suspend fun chatCompletion(
        @Body request: ChatRequest
    ): ChatResponse
}

data class ChatRequest(
    val model: String = "openai",
    val messages: List<Message>,
    val temperature: Float = 1.0f,
    val stream: Boolean = false
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String? = null,
    val choices: List<Choice>,
    val created: Long? = null,
    val model: String? = null
)

data class Choice(
    val message: MessageResponse,
    val index: Int? = null,
    @SerializedName("finish_reason")
    val finishReason: String? = null
)

data class MessageResponse(
    val role: String,
    val content: String
)
