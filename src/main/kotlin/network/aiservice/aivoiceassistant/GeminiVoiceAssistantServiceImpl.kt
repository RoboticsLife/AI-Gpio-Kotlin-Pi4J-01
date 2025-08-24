package network.aiservice.aivoiceassistant

import brain.ai.data.local.AIConfiguration
import brain.emitters.NetworkEmitters
import network.InternetConnection
import network.aiservice.aivoiceassistant.data.GeminiVoiceGeneratorRequest
import java.nio.charset.StandardCharsets

class GeminiVoiceAssistantServiceImpl(aiVoiceConfig: AIConfiguration.AdditionalAIService): AIVoiceAssistantService {

    private val aiConfig: AIConfiguration.AdditionalAIService = aiVoiceConfig
    private val client = InternetConnection.getRetrofitClient(aiVoiceConfig.aiServerBaseURL.toString())
    private val apiService = client.create(Api::class.java)

    private fun verifyAIFlow() {
        if (NetworkEmitters.aiVoiceEmitter.replayCache.firstOrNull() == null) {
            NetworkEmitters.emitAIVoiceGeneratorResponse(false)
        }
    }


    private fun generateAITextToVoiceRequest(text: String): GeminiVoiceGeneratorRequest {
        return GeminiVoiceGeneratorRequest(
            contents = listOf(
                GeminiVoiceGeneratorRequest.Content(
                    role = "user",
                    parts = listOf(GeminiVoiceGeneratorRequest.Content.Part(
                        text = text
                    ))
                )),
            generationConfig = GeminiVoiceGeneratorRequest.GenerationConfig(
                responseModalities = listOf("audio"),
                temperature = 1,
                speechConfig = GeminiVoiceGeneratorRequest.GenerationConfig.SpeechConfig(
                    voiceConfig = GeminiVoiceGeneratorRequest.GenerationConfig.SpeechConfig.VoiceConfig(
                        prebuiltVoiceConfig = GeminiVoiceGeneratorRequest.GenerationConfig.SpeechConfig.VoiceConfig.PrebuiltVoiceConfig(
                            voiceName = "Umbriel" //TODO add different voices
                        )
                    )
                )
            )
        )
    }

    override fun convertStringToSoundSource(text: String) {
        val request = generateAITextToVoiceRequest(text)
        val requestTime = System.currentTimeMillis()

        val response = apiService.getAIVoiceResponse(
            //routeUrl = aiConfig.aiVoiceGenerationApiRoute.toString(),
            key = aiConfig.apiKey.toString(),
            geminiVoiceGeneratorRequest = request
        ).execute()

        val responseTime = System.currentTimeMillis()
        if (NetworkEmitters.aiVoiceEmitter.replayCache.firstOrNull() == null) verifyAIFlow()

        //TODO Tests
       // println(response.body())

        //TODO tests generate it
        val audioStream = response.body()?.firstOrNull()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.inlineData?.data
        println(audioStream)
     //   val bytes: ByteArray? = audioStream.getBytes(StandardCharsets.UTF_8)


    }
}