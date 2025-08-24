package brain.ai.data.local

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AIConfiguration(
    val aiChatRequestApiRoute: String? = null,
    val aiConfigDescription: String? = null,
    val aiConfigName: String? = null,
    val aiConfigVersion: String? = null,
    val aiConnectionType: String? = null,
    val aiImageMode: Boolean? = null,
    val aiModel: String? = null,
    val aiServerBaseURL: String? = null,
    val aiSingleRequestApiRoute: String? = null,
    val aiTextMode: Boolean? = null,
    val aiType: String? = null,
    val aiVoiceMode: Boolean? = null,
    val isAIOfflineModeAvailable: Boolean? = null,
    val additionalAIServices: List<AdditionalAIService?>? = null,
    val additionalSettings: List<Pair<String?, String?>?>? = null,
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AdditionalAIService(
        val aiConfigDescription: String? = null,
        val aiConfigName: String? = null,
        val aiConfigVersion: String? = null,
        val aiConnectionType: String? = null,
        val aiImageMode: Boolean? = null,
        val aiModel: String? = null,
        val aiServerBaseURL: String? = null,
        val aiTextMode: Boolean? = null,
        val aiType: String? = null,
        val aiVoiceGenerationApiRoute: String? = null,
        val aiVoiceMode: Boolean? = null,
        val apiKey: String? = null,
        val isAIOfflineModeAvailable: Boolean? = null
    )
}