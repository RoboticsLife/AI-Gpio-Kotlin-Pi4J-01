package network.aiservice.ollama

import network.aiservice.ollama.data.OllamaGenerateRequest
import network.aiservice.ollama.data.OllamaGenerateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @POST("{routeUrl}")
    fun getAIResponse(@Path("routeUrl") routeUrl: String, @Body ollamaGenerateRequest: OllamaGenerateRequest): Call<OllamaGenerateResponse>
}