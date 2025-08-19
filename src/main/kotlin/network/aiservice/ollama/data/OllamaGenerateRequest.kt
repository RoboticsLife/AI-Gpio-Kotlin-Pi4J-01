package network.aiservice.ollama.data

import com.fasterxml.jackson.annotation.JsonProperty
import network.aiservice.data.AbstractRequest

data class OllamaGenerateRequest(
    @JsonProperty("images")
    val images: List<String?>? = null,
    @JsonProperty("model")
    val model: String? = null,
    @JsonProperty("options")
    val options: Options? = null,
    @JsonProperty("prompt")
    val prompt: String? = null,
    @JsonProperty("stream")
    val stream: Boolean? = null
): AbstractRequest() {
    data class Options(
        @JsonProperty("frequency_penalty")
        val frequencyPenalty: Double? = null,
        @JsonProperty("low_vram")
        val lowVram: Boolean? = null,
        @JsonProperty("main_gpu")
        val mainGpu: Int? = null,
        @JsonProperty("min_p")
        val minP: Double? = null,
        @JsonProperty("mirostat")
        val mirostat: Int? = null,
        @JsonProperty("mirostat_eta")
        val mirostatEta: Double? = null,
        @JsonProperty("mirostat_tau")
        val mirostatTau: Double? = null,
        @JsonProperty("num_batch")
        val numBatch: Int? = null,
        @JsonProperty("num_ctx")
        val numCtx: Int? = null,
        @JsonProperty("num_gpu")
        val numGpu: Int? = null,
        @JsonProperty("num_keep")
        val numKeep: Int? = null,
        @JsonProperty("num_predict")
        val numPredict: Int? = null,
        @JsonProperty("num_thread")
        val numThread: Int? = null,
        @JsonProperty("numa")
        val numa: Boolean? = null,
        @JsonProperty("penalize_newline")
        val penalizeNewline: Boolean? = null,
        @JsonProperty("presence_penalty")
        val presencePenalty: Double? = null,
        @JsonProperty("repeat_last_n")
        val repeatLastN: Int? = null,
        @JsonProperty("repeat_penalty")
        val repeatPenalty: Double? = null,
        @JsonProperty("seed")
        val seed: Int? = null,
        @JsonProperty("stop")
        val stop: List<String?>? = null,
        @JsonProperty("temperature")
        val temperature: Double? = null,
        @JsonProperty("top_k")
        val topK: Int? = null,
        @JsonProperty("top_p")
        val topP: Double? = null,
        @JsonProperty("typical_p")
        val typicalP: Double? = null,
        @JsonProperty("use_mlock")
        val useMlock: Boolean? = null,
        @JsonProperty("use_mmap")
        val useMmap: Boolean? = null,
        @JsonProperty("vocab_only")
        val vocabOnly: Boolean? = null
    )
}