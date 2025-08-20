package brain

import avatar.Avatar
import brain.ai.data.local.AIConfiguration
import brain.ai.data.local.AITextRequestParams
import brain.data.remote.DistanceSensor
import brain.emitters.DistanceEmitters
import brain.utils.toCm
import kotlinx.coroutines.*
import network.aiservice.AIService
import network.aiservice.ollama.AIOllamaNetworkService
import network.databases.DatabaseConnector
import network.databases.DatabaseInitializer
import org.slf4j.Logger
import runtime.setup.Injector
import runtime.setup.Settings
import runtime.setup.Settings.AI_REMOTE_CONNECTION_TYPE

class Brain {

    private lateinit var avatar: Avatar
    private lateinit var dataBaseFirebaseFirestore: DatabaseConnector
    private lateinit var aiConfig: AIConfiguration
    private lateinit var aiService: AIService

    //Threads
    private var devicesThreadScopeArray: MutableMap<String, Job?> = mutableMapOf()

    private fun init() {
        initDatabases()
        initAI()
    }

    fun build(avatar: Avatar): Brain {
        this.avatar = avatar
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

    fun readFromMemory(parameterName: String, key: Any?) {
        dataBaseFirebaseFirestore.readFromDB(parameterName, key)
    }

    fun rememberToMemory(parameterName: String, data: Any) {
        dataBaseFirebaseFirestore.writeToDB(parameterName, data)
    }

    fun startTrackDevice(parameterName: String, devicePosition: Int? = null, loggingPeriodInMillis: Long = 1000) {
        when (parameterName) {
            PARAMETER_SENSOR_DISTANCE -> {
                subscribeToDistanceEmitters(devicePosition, loggingPeriodInMillis)
            }
        }
    }

    fun stopTrackDevice(parameterName: String, devicePosition: Int? = null) {
        devicesThreadScopeArray["$parameterName${devicePosition.toString()}"]?.cancel()
        devicesThreadScopeArray["$parameterName${devicePosition.toString()}"] = null
        devicesThreadScopeArray.remove("$parameterName${devicePosition.toString()}")
    }

    fun askAI(question: String, params: AITextRequestParams? = null) {
        aiService.askAI(question, params)
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