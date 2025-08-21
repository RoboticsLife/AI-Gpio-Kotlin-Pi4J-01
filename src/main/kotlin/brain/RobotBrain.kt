package brain

import avatar.Avatar
import brain.ai.data.local.AIConfiguration
import brain.ai.data.local.AITextRequestParams
import brain.data.remote.DistanceSensor
import brain.emitters.DistanceEmitters
import brain.perceptions.Perceptions
import brain.perceptions.RobotPerceptions
import brain.utils.toCm
import kotlinx.coroutines.*
import network.aiservice.AIService
import network.aiservice.ollama.AIOllamaNetworkService
import network.databases.DatabaseConnector
import network.databases.DatabaseInitializer
import runtime.setup.Injector
import runtime.setup.Settings
import runtime.setup.Settings.AI_REMOTE_CONNECTION_TYPE

class RobotBrain: Brain {

    private lateinit var avatar: Avatar
    private lateinit var dataBaseFirebaseFirestore: DatabaseConnector
    private lateinit var aiConfig: AIConfiguration
    private lateinit var aiService: AIService
    override lateinit var perceptions: Perceptions


    //Threads
    private var devicesThreadScopeArray: MutableMap<String, Job?> = mutableMapOf()

    private fun init() {
        initDatabases()
        initAI()
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
            aiService = AIOllamaNetworkService(aiConfig)
        } else {
            //TODO: provide local AI service if need
            aiService = AIOllamaNetworkService(aiConfig)
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


    companion object {
        const val PARAMETER_SENSOR_DISTANCE = "sensorDistance"
        //add more...
    }

}