package network.aiservice.aivoiceassistant.data


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class GeminiVoiceGeneratorRequest(
    @JsonProperty("contents")
    val contents: List<Content?>? = null,
    @JsonProperty("generationConfig")
    val generationConfig: GenerationConfig? = null
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class Content(
        @JsonProperty("parts")
        val parts: List<Part?>? = null,
        @JsonProperty("role")
        val role: String? = null
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        data class Part(
            @JsonProperty("text")
            val text: String? = null
        )
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class GenerationConfig(
        @JsonProperty("responseModalities")
        val responseModalities: List<String?>? = null,
        @JsonProperty("speech_config")
        val speechConfig: SpeechConfig? = null,
        @JsonProperty("temperature")
        val temperature: Int? = null
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        data class SpeechConfig(
            @JsonProperty("voice_config")
            val voiceConfig: VoiceConfig? = null
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            data class VoiceConfig(
                @JsonProperty("prebuilt_voice_config")
                val prebuiltVoiceConfig: PrebuiltVoiceConfig? = null
            ) {
                @JsonIgnoreProperties(ignoreUnknown = true)
                @JsonInclude(JsonInclude.Include.NON_EMPTY)
                data class PrebuiltVoiceConfig(
                    @JsonProperty("voice_name")
                    val voiceName: String? = null
                )
            }
        }
    }
}