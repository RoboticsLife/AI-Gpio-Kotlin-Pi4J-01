package network.aiservice.ollama

import brain.ai.data.local.AIConfiguration
import brain.ai.data.local.AIFlowDataModel
import brain.ai.data.local.AITextRequestParams
import brain.emitters.NetworkEmitters
import network.InternetConnection
import network.aiservice.AIService
import network.aiservice.ollama.data.OllamaGenerateRequest
import runtime.setup.Settings
import java.util.*


class AIOllamaNetworkService(aiConfiguration: AIConfiguration): AIService {

    private val aiConfig: AIConfiguration = aiConfiguration
    private val client = InternetConnection.getWeatherClient(aiConfiguration.aiServerBaseURL.toString())
    private val apiService = client.create(Api::class.java)

    private fun verifyAIFlow() {
        if (NetworkEmitters.aiEmitter.replayCache.firstOrNull() == null) {
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

    private fun generateAITextAndImageRequest(text: String, image64String: String): OllamaGenerateRequest {
        return OllamaGenerateRequest(
            model = aiConfig.aiModel,
            prompt = text,
            images = listOf(image64String),
            stream = false
        )
    }

    private fun applyRequestRulesToQuery(question: String, params: AITextRequestParams?): String {
        if (params == null) return question
        if (params.aiTextResponseLengthLimit == 0) return question
        if (params.aiTextResponseLengthLimit > 0)
            return question + " " + String.format(Settings.AI_FORMATTED_STRING_QUERY_WORDS_LIMITATION, params.aiTextResponseLengthLimit)

        return question
    }

    override fun askAI(question: String, params: AITextRequestParams?) {
        verifyAIFlow()

        val formattedQuestion = applyRequestRulesToQuery(question, params)
        val ollamaGenerateRequest = generateAITextRequest(formattedQuestion)
        val requestTime = System.currentTimeMillis()

        val response = apiService.getAIResponse(
            routeUrl = aiConfig.aiSingleRequestApiRoute.toString(),
            ollamaGenerateRequest = ollamaGenerateRequest
            ).execute()

        val responseTime = System.currentTimeMillis()
        if (NetworkEmitters.aiEmitter.replayCache.firstOrNull() == null) verifyAIFlow()

        //Emit new AI data
        NetworkEmitters.emitAIResponse(NetworkEmitters.aiEmitter.replayCache.firstOrNull().also { model ->
            model?.aiRequestResponseLinkedHashSet?. add(AIFlowDataModel.AIRequestResponseChain(
                id = NetworkEmitters.aiEmitter.replayCache.firstOrNull()!!.aiRequestResponseLinkedHashSet.size,
                requestTime = requestTime,
                responseTime = responseTime,
                request = ollamaGenerateRequest,
                response = response.body(),
                isSuccessful = response.isSuccessful,
                httpCode = response.code(),
                message = response.message()
            ))
        })

    }

    override fun imageClassification(question: String,  params: AITextRequestParams?) {
        println(question)

        val inputStream = javaClass.classLoader.getResource("cntower.jpg")?.openStream()
        //val inputStream = javaClass.classLoader.getResource("avatar.jpg")?.openStream()
        val base64Image = Base64.getEncoder().encodeToString(inputStream?.readAllBytes())
        println(base64Image)


        verifyAIFlow()

        val formattedQuestion = applyRequestRulesToQuery(question, params)
        val ollamaGenerateRequest = generateAITextAndImageRequest(formattedQuestion, base64Image)
        val requestTime = System.currentTimeMillis()

        val response = apiService.getAIResponse(
            routeUrl = aiConfig.aiSingleRequestApiRoute.toString(),
            ollamaGenerateRequest = ollamaGenerateRequest
        ).execute()

        val responseTime = System.currentTimeMillis()
        if (NetworkEmitters.aiEmitter.replayCache.firstOrNull() == null) verifyAIFlow()

        //Emit new AI data
        NetworkEmitters.emitAIResponse(NetworkEmitters.aiEmitter.replayCache.firstOrNull().also { model ->
            model?.aiRequestResponseLinkedHashSet?. add(AIFlowDataModel.AIRequestResponseChain(
                id = NetworkEmitters.aiEmitter.replayCache.firstOrNull()!!.aiRequestResponseLinkedHashSet.size,
                requestTime = requestTime,
                responseTime = responseTime,
                request = ollamaGenerateRequest,
                response = response.body(),
                isSuccessful = response.isSuccessful,
                httpCode = response.code(),
                message = response.message()
            ))
        })

    }

}