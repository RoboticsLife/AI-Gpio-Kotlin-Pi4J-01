package network.aiservice.ollama

import brain.ai.data.local.AIConfiguration
import brain.ai.data.local.AIFlowDataModel
import brain.data.local.WeatherData
import brain.emitters.NetworkEmitters
import network.InternetConnection
import network.aiservice.data.AbstractRequest
import network.aiservice.ollama.data.OllamaGenerateRequest
import runtime.setup.Settings

class AIOllamaNetworkService(aiConfiguration: AIConfiguration) {

    private val aiConfig: AIConfiguration = aiConfiguration
    private val client = InternetConnection.getWeatherClient(aiConfiguration.aiServerBaseURL.toString())
    private val apiService = client.create(Api::class.java)
    var aiTextResponseLengthLimit: Int = 0
        private set

    private fun verifyAIFlow() {
        if (NetworkEmitters.aiEmitter.value == null) {
            NetworkEmitters.emitAIResponse(
                AIFlowDataModel(
                    initialTime = System.currentTimeMillis(),
                    aiConfig = aiConfig
            ))
        }
    }

    private fun generateAITextRequest(text: String): OllamaGenerateRequest {
        //TODO
        return OllamaGenerateRequest()

    }

    private fun buildAIFlowDataModel(): AIFlowDataModel.AIRequestResponseChain {
        return AIFlowDataModel.AIRequestResponseChain()
    }


    fun askAI(question: String) {
        verifyAIFlow()

        val ollamaGenerateRequest = generateAITextRequest(question)
        val response = apiService.getAIResponse(
            routeUrl = aiConfig.aiSingleRequestApiRoute.toString(),
            ollamaGenerateRequest = ollamaGenerateRequest
            ).execute()

        NetworkEmitters.emitAIResponse()

        NetworkEmitters.emitWeatherResponse(
            WeatherData(
                weatherResponse = if (response.isSuccessful) response.body() else null,
                isSuccessful = response.isSuccessful,
                httpCode = response.code(),
                message = response.message()
            )
        )
    }

    fun imageClassification(decodedImageBase64String: String) {
        //TODO
    }

    fun setAITextResponseLengthLimit() {
        //TODO
    }
}