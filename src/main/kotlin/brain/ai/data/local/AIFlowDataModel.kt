package brain.ai.data.local

import network.aiservice.data.AbstractRequest
import network.aiservice.data.AbstractResponse

data class AIFlowDataModel(
    val initialTime: Long = 0,
    val aiConfig: AIConfiguration = AIConfiguration(),
    val aiRequestResponseLinkedHashSet: LinkedHashSet<AIRequestResponseChain> = LinkedHashSet()
    //TODO add AI chat LinkedHashSet

) {
    data class AIRequestResponseChain(
        val id: Int = 0,
        val requestTime: Long = 0,
        val responseTime: Long = 0,
        val request: AbstractRequest? = null,
        val response: AbstractResponse? = null,
        val isSuccessful: Boolean = false,
        val httpCode: Int = 0,
        val message: String = ""
    )
}
