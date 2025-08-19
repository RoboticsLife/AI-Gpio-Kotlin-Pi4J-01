package network.aiservice.ollama.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import network.aiservice.data.AbstractResponse

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class OllamaGenerateResponse(
    val context: List<Long?>? = null,
    val created_at: String? = null,
    val done: Boolean? = null,
    val done_reason: String? = null,
    val eval_count: Long? = null,
    val eval_duration: Long? = null,
    val load_duration: Long? = null,
    val model: String? = null,
    val prompt_eval_count: Int? = null,
    val prompt_eval_duration: Long? = null,
    val response: String? = null,
    val total_duration: Long? = null
): AbstractResponse()