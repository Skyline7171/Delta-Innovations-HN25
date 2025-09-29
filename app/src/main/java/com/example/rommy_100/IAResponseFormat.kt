package com.example.rommy_100

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Para el cuerpo de la petición
@Serializable
data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessageContent>,
    // Podríamos añadir  añadir otros parámetros como temperature, max_tokens, más tarde
)

@Serializable
data class ChatMessageContent(
    val role: String,
    val content: String
)

// Para la respuesta
@Serializable
data class ChatCompletionResponse(
    val id: String? = null,
    val choices: List<Choice>? = null,
    val error: ApiError? = null // Para manejar errores de la API
)

@Serializable
data class Choice(
    val message: ChatMessageContent? = null,
    @SerialName("finish_reason")
    val finishReason: String? = null
)

@Serializable
data class ApiError(
    val message: String,
    val type: String? = null,
    val param: String? = null,
    val code: String? = null
)