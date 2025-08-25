package network.aiservice.aivoiceassistant

import brain.ai.data.local.AIConfiguration
import brain.ai.data.local.GeneratedVoiceResponseDataModel
import brain.emitters.NetworkEmitters
import network.InternetConnection
import network.aiservice.aivoiceassistant.data.GeminiVoiceGeneratorRequest

class GeminiVoiceAssistantServiceImpl(aiVoiceConfig: AIConfiguration.AdditionalAIService): AIVoiceAssistantService {

    private val aiConfig: AIConfiguration.AdditionalAIService = aiVoiceConfig
    private val client = InternetConnection.getRetrofitClient(aiVoiceConfig.aiServerBaseURL.toString())
    private val apiService = client.create(Api::class.java)

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
                            voiceName = aiConfig.voice
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
            key = aiConfig.apiKey.toString(),
            geminiVoiceGeneratorRequest = request
        ).execute()

        val responseTime = System.currentTimeMillis()

        NetworkEmitters.emitAIVoiceGeneratorResponse(GeneratedVoiceResponseDataModel(
            aiConfig = aiConfig,
            requestResponsePair = GeneratedVoiceResponseDataModel.GeneratedVoiceRequestResponseChain(
                id = requestTime,
                requestTime = requestTime,
                responseTime = responseTime,
                request = request,
                response = response.body(),
                isSuccessful = response.isSuccessful,
                httpCode = response.code(),
                message = response.message()
            )
        ))
    }
}