package brain

import avatar.Avatar
import brain.ai.data.local.AIConfiguration
import brain.ai.data.local.AITextRequestParams
import brain.data.remote.DistanceSensor
import brain.emitters.DistanceEmitters
import brain.emitters.NetworkEmitters
import brain.perceptions.Perceptions
import brain.perceptions.RobotPerceptions
import brain.perceptions.speaking.TTSGeminiOnlineEngine
import brain.perceptions.speaking.TTSVoice
import brain.utils.toCm
import kotlinx.coroutines.*
import network.aiservice.AIService
import network.aiservice.aivoiceassistant.AIVoiceAssistantService
import network.aiservice.aivoiceassistant.GeminiVoiceAssistantServiceImpl
import network.aiservice.ollama.AIOllamaNetworkServiceImpl
import network.aiservice.ollama.data.OllamaGenerateResponse
import network.databases.DatabaseConnector
import network.databases.DatabaseInitializer
import org.example.runtime.collectData
import runtime.setup.Injector
import runtime.setup.Settings
import runtime.setup.Settings.AI_REMOTE_CONNECTION_TYPE

class RobotBrain: Brain {

    private lateinit var avatar: Avatar
    private lateinit var dataBaseFirebaseFirestore: DatabaseConnector
    private lateinit var aiConfig: AIConfiguration
    private lateinit var aiService: AIService
    private lateinit var aiVoiceAssistantService: AIVoiceAssistantService
    override lateinit var perceptions: Perceptions
    private lateinit var voice: TTSVoice



    //Threads
    private var devicesThreadScopeArray: MutableMap<String, Job?> = mutableMapOf()

    private fun init() {
        initDatabases()
        initAI()
        initAIVoiceGenerator()
        collectData()
    }

    override fun build(avatar: Avatar): Brain {
        this.avatar = avatar
        perceptions = RobotPerceptions()
        init()
        return this
    }

    private fun initDatabases() {
        dataBaseFirebaseFirestore = DatabaseConnector(type = DatabaseConnector.DatabaseTypes.FIREBASE_FIRESTORE_DB)
    }

    private fun initAI() {
        val aiConfigFileName =
            if (this.avatar.configuration?.customAiConfigName.isNullOrEmpty()) Settings.AI_DEFAULT_CONFIG_FILE_NAME
            else this.avatar.configuration?.customAiConfigName
        aiConfig = Injector.getRuntimeAIConfiguration().getAIConfiguration(aiConfigFileName.toString())
        println(aiConfig.toString()) //Replace with internal logs emitter / collector
        if (aiConfig.aiConnectionType == AI_REMOTE_CONNECTION_TYPE) {
            aiService = AIOllamaNetworkServiceImpl(aiConfig)
        } else {
            //TODO: provide local AI service if need
            aiService = AIOllamaNetworkServiceImpl(aiConfig)
        }
    }

    private fun initAIVoiceGenerator() {
        val config = aiConfig.additionalAIServices?.first { it?.aiType == "voiceGenerator"}
        if (config != null) {
            aiVoiceAssistantService = GeminiVoiceAssistantServiceImpl(config)
            voice = TTSGeminiOnlineEngine()
        } else {
            aiVoiceAssistantService = GeminiVoiceAssistantServiceImpl(AIConfiguration.AdditionalAIService())
            voice = TTSGeminiOnlineEngine()
        }
    }

    override fun readFromMemory(parameterName: String, key: Any?) {
        dataBaseFirebaseFirestore.readFromDB(parameterName, key)
    }

    override fun rememberToMemory(parameterName: String, data: Any) {
        dataBaseFirebaseFirestore.writeToDB(parameterName, data)
    }

    override fun startTrackDevice(parameterName: String, devicePosition: Int?, loggingPeriodInMillis: Long) {
        when (parameterName) {
            PARAMETER_SENSOR_DISTANCE -> {
                subscribeToDistanceEmitters(devicePosition, loggingPeriodInMillis)
            }
        }
    }

    override fun stopTrackDevice(parameterName: String, devicePosition: Int?) {
        devicesThreadScopeArray["$parameterName${devicePosition.toString()}"]?.cancel()
        devicesThreadScopeArray["$parameterName${devicePosition.toString()}"] = null
        devicesThreadScopeArray.remove("$parameterName${devicePosition.toString()}")
    }

    override fun askUseAI(question: String, params: AITextRequestParams?) {
        aiService.askAI(question, params)
    }

    override fun lookUseAI(question: String, params: AITextRequestParams?) {
        aiService.imageClassification(question, params)
    }

    override fun generateVoiceFromString(text: String) {
        aiVoiceAssistantService.convertStringToSoundSource(text)
    }

    private fun subscribeToDistanceEmitters(sensorPosition: Int? = null, loggingPeriodInMillis: Long = 1000) {
        var launchTime = System.currentTimeMillis()
        val threadScope = CoroutineScope(Job() + Dispatchers.IO).launch {
            DistanceEmitters.distanceSensor.collect { distance ->
                if (System.currentTimeMillis() >= launchTime+loggingPeriodInMillis ) {
                    rememberToMemory(
                        parameterName = DatabaseInitializer.DB_TABLE_NAME_DISTANCE_SENSORS,
                        data = DistanceSensor(
                            config_id = avatar.configuration?.configName.toString(),
                            sensor_id = distance.sensorPosition.toString(),
                            time = distance.echoLowNanoTime,
                            unit = "cm",
                            value = distance.toCm()
                        )
                    )
                    launchTime = System.currentTimeMillis()
                }
            }

        }

        devicesThreadScopeArray["$PARAMETER_SENSOR_DISTANCE${sensorPosition.toString()}"] = threadScope
    }

    private fun collectData() {
        val jobAIVoiceGeneratorResponseCollector = CoroutineScope(Job() + Dispatchers.IO).launch {
            NetworkEmitters.aiVoiceEmitter.collect { generatedVoice ->
                val audioStream = generatedVoice.requestResponsePair.response?.firstOrNull()
                    ?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.inlineData?.data
                voice.talk(audioStream ?: "")

            }
        }
    }

    companion object {
        const val PARAMETER_SENSOR_DISTANCE = "sensorDistance"
        //add more...
    }

}