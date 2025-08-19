package network.aiservice

interface AIService {

    fun askAI(question: String)

    fun imageClassification(decodedImageBase64String: String)

    fun setAITextResponseLengthLimit(wordsLength: Int)
}