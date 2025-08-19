package brain.ai

import brain.ai.data.local.AIConfiguration
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import runtime.setup.Settings

class RuntimeAIConfigurationWorkerImpl: RuntimeAIConfigurationWorker {

    override fun getAIConfiguration(fileName: String): AIConfiguration {
        val mapper = jacksonObjectMapper()
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule())
        try {
            var jsonString = javaClass.classLoader.getResource(fileName)?.readText()
            if (!jsonString.isNullOrEmpty()) {
                jsonString = javaClass.classLoader.getResource(Settings.AI_DEFAULT_CONFIG_FILE_NAME)?.readText()
            }
            val jsonTextConfig: AIConfiguration = mapper.readValue(jsonString, AIConfiguration::class.java)
            return jsonTextConfig
        } catch (e: Exception) {
            return AIConfiguration()
        }
    }
}