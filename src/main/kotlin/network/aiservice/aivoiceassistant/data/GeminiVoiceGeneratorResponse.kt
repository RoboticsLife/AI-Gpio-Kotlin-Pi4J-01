package network.aiservice.aivoiceassistant.data

class GeminiVoiceGeneratorResponse : ArrayList<GeminiVoiceGeneratorResponse.GeminiVoiceGeneratorResponseItem>() {
    data class GeminiVoiceGeneratorResponseItem(
        val candidates: List<Candidate?>? = null,
        val modelVersion: String? = null,
        val responseId: String? = null,
        val usageMetadata: UsageMetadata? = null
    ) {
        data class Candidate(
            val content: Content? = null,
            val finishReason: String? = null,
            val index: Int? = null
        ) {
            data class Content(
                val parts: List<Part?>? = null,
                val role: String? = null
            ) {
                data class Part(
                    val inlineData: InlineData? = null
                ) {
                    data class InlineData(
                        val `data`: String? = null,
                        val mimeType: String? = null
                    )
                }
            }
        }
    
        data class UsageMetadata(
            val candidatesTokenCount: Int? = null,
            val candidatesTokensDetails: List<CandidatesTokensDetail?>? = null,
            val promptTokenCount: Int? = null,
            val promptTokensDetails: List<PromptTokensDetail?>? = null,
            val totalTokenCount: Int? = null
        ) {
            data class CandidatesTokensDetail(
                val modality: String? = null,
                val tokenCount: Int? = null
            )
    
            data class PromptTokensDetail(
                val modality: String? = null,
                val tokenCount: Int? = null
            )
        }
    }
}