package network.aiservice.ollama.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class OllamaGenerateResponse(
    val context: List<Int?>? = null,
    val created_at: String? = null,
    val done: Boolean? = null,
    val done_reason: String? = null,
    val eval_count: Int? = null,
    val eval_duration: Int? = null,
    val load_duration: Long? = null,
    val model: String? = null,
    val prompt_eval_count: Int? = null,
    val prompt_eval_duration: Long? = null,
    val response: String? = null,
    val total_duration: Long? = null
)