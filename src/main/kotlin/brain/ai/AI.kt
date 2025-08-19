package brain.ai

import brain.ai.data.local.AIConfiguration

abstract class AI(aiConfiguration: AIConfiguration) {

    init {
        initAI()
    }

    abstract fun initAI()
}