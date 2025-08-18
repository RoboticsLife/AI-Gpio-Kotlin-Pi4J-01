package network.aiservice.ollama

import network.aiservice.ollama.data.OllamaGenerateRequest
import network.aiservice.ollama.data.OllamaGenerateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("/generate")
    fun getAIResponse(@Body ollamaGenerateRequest: OllamaGenerateRequest): Call<OllamaGenerateResponse>
}