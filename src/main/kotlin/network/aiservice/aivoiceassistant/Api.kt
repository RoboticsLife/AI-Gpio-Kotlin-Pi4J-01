package network.aiservice.aivoiceassistant

import network.aiservice.aivoiceassistant.data.GeminiVoiceGeneratorRequest
import network.aiservice.aivoiceassistant.data.GeminiVoiceGeneratorResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

   // @POST("{routeUrl}")
    @POST("v1beta/models/gemini-2.5-flash-preview-tts:streamGenerateContent")
    fun getAIVoiceResponse(
     //   @Path("routeUrl") routeUrl: String,
        @Query("key") key: String,
        @Body geminiVoiceGeneratorRequest: GeminiVoiceGeneratorRequest
    ): Call<GeminiVoiceGeneratorResponse>
}