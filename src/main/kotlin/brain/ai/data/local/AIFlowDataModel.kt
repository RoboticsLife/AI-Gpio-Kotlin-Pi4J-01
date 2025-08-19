package brain.ai.data.local

import network.aiservice.data.AbstractRequest
import network.aiservice.data.AbstractResponse

data class AIFlowDataModel(
    val initialTime: Long = 0,
    val aiConfig: AIConfiguration = AIConfiguration(),
    val aiRequestResponseLinkedHashSet: LinkedHashSet<String> = LinkedHashSet()
    //TODO add AI chat LinkedHashSet

) {
    data class AIRequestResponseChain(
        val id: Int,
        val requestTime: Long,
        val responseTime: Long,
        val request: AbstractRequest,
        val response: AbstractResponse

    )
}
