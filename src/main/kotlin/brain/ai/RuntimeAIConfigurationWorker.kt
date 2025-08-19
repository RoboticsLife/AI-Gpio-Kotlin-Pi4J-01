package brain.ai

import brain.ai.data.local.AIConfiguration

interface RuntimeAIConfigurationWorker {

    fun getAIConfiguration(fileName: String): AIConfiguration
}