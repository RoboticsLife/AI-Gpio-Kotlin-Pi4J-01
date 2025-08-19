package network.aiservice.ollama

import brain.ai.data.local.AIConfiguration
import brain.ai.data.local.AIFlowDataModel
import brain.emitters.NetworkEmitters
import network.InternetConnection
import network.aiservice.AIService
import network.aiservice.data.AbstractRequest
import network.aiservice.ollama.data.OllamaGenerateRequest
import runtime.setup.Settings

class AIOllamaNetworkService(aiConfiguration: AIConfiguration): AIService {

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

        return OllamaGenerateRequest(
            model = aiConfig.aiModel,
            prompt = text,
            stream = false
            )

    }

    private fun buildAIFlowDataModel(): AIFlowDataModel.AIRequestResponseChain {
        return AIFlowDataModel.AIRequestResponseChain()
    }


    override fun askAI(question: String) {
        verifyAIFlow()

        println("ASKING!!!")

        val ollamaGenerateRequest = generateAITextRequest(question)
        val response = apiService.getAIResponse(
            routeUrl = aiConfig.aiSingleRequestApiRoute.toString(),
            ollamaGenerateRequest = ollamaGenerateRequest
            ).execute()

        println(response.body()?.response.toString())
       // NetworkEmitters.emitAIResponse()

      //  NetworkEmitters.emitWeatherResponse(
        //    WeatherData(
          //      weatherResponse = if (response.isSuccessful) response.body() else null,
            //    isSuccessful = response.isSuccessful,
              //  httpCode = response.code(),
              //  message = response.message()
           // )
       // )
    }

    override fun imageClassification(decodedImageBase64String: String) {
        //TODO
    }

    override fun setAITextResponseLengthLimit(wordsLength: Int) {
        //TODO
    }
}