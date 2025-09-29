package com.example.rommy_100

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object OpenRouterApiService {
    // TODO: Acórdate de quitar la API_KEY de aquí si en un dado caso sale a producción
    private const val API_KEY = "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"
    private const val BASE_URL = "https://openrouter.ai/api/v1"

    private val client = HttpClient(CIO) { // También podríamos usar Android o OkHttp engine
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // Para manejar campos no definidos en tus data classes
            })
        }

        install(HttpTimeout) {
            // Tiempo total para que la petición completa termine (conexión + envío + recepción)
            // Para respuestas de IA que pueden tardar
            // 90 segundos
            requestTimeoutMillis = 90000L // 90 * 1000 ms

            // Tiempo para establecer la conexión
            // 20 segundos
            connectTimeoutMillis = 20000L

            // Tiempo de inactividad entre paquetes de datos una vez conectado
            // 60 segundos
            socketTimeoutMillis = 60000L
        }
    }

    suspend fun getChatCompletion(userInput: String): Result<String> {
        val requestBody = ChatCompletionRequest(
            model = "openai/gpt-oss-120b", // Revisar gpt-3.5-turbo

            messages = listOf(
                ChatMessageContent(
                    role = "system",
                    content = "Eres Roomy, un asistente médico empático que responde en español con orientación clara y respetuosa. Puedes sugerir tratamientos básicos cuando los sintomas no son alarmantes o graves. No diagnosticas ni recetas. Tu función es orientar sobre síntomas comunes, autocuidado y cuándo acudir a un centro médico."
                ),
                ChatMessageContent(
                    role = "user",
                    content = userInput
                )
            )
        )

        return try {
            val response: ChatCompletionResponse = client.post("$BASE_URL/chat/completions") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $API_KEY")
                setBody(requestBody)
            }.body()

            val reply = response.choices?.firstOrNull()?.message?.content
            if (reply != null) {
                Result.success(reply)
            } else if (response.error != null) {
                Result.failure(Exception("Error de API: ${response.error.message}"))
            }
            else {
                Result.failure(Exception("Respuesta inesperada o vacía de la API."))
            }
        } catch (e: Exception) {
            // Log.e("ApiService", "Error en la petición: ${e.localizedMessage}", e) // Para debugging
            Result.failure(e)
        }
    }
}
