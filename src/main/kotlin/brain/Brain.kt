package brain

import avatar.Avatar
import brain.ai.data.local.AITextRequestParams
import brain.perceptions.Perceptions

interface Brain {

    var perceptions: Perceptions

    fun build(avatar: Avatar): Brain

    fun readFromMemory(parameterName: String, key: Any?)

    fun rememberToMemory(parameterName: String, data: Any)

    fun startTrackDevice(parameterName: String, devicePosition: Int? = null, loggingPeriodInMillis: Long = 1000)

    fun stopTrackDevice(parameterName: String, devicePosition: Int? = null)

    fun askUseAI(question: String, params: AITextRequestParams? = null)

    fun lookUseAI(question: String, params: AITextRequestParams? = null)

    fun generateVoiceFromString(text: String)

}