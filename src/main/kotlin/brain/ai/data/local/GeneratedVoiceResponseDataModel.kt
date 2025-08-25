package brain.ai.data.local

import network.aiservice.aivoiceassistant.data.GeminiVoiceGeneratorResponse
import network.aiservice.data.AbstractRequest

data class GeneratedVoiceResponseDataModel(
    val aiConfig: AIConfiguration.AdditionalAIService = AIConfiguration.AdditionalAIService(),
    val requestResponsePair: GeneratedVoiceRequestResponseChain = GeneratedVoiceRequestResponseChain()
) {
    data class GeneratedVoiceRequestResponseChain(
        val id: Long = 0,
        val requestTime: Long = 0,
        val responseTime: Long = 0,
        val request: AbstractRequest? = null,
        val response: GeminiVoiceGeneratorResponse? = null,
        val isSuccessful: Boolean = false,
        val httpCode: Int = 0,
        val message: String = ""
    )
}
