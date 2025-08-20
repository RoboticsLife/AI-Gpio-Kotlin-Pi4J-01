package network.aiservice

import brain.ai.data.local.AITextRequestParams

interface AIService {

    fun askAI(question: String,  params: AITextRequestParams? = null)

    fun imageClassification(decodedImageBase64String: String)

}