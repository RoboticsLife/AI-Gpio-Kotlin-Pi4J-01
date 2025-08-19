package runtime.setup

import brain.ai.RuntimeAIConfigurationWorker
import brain.ai.RuntimeAIConfigurationWorkerImpl
import com.pi4j.Pi4J
import com.pi4j.context.Context
import com.pi4j.util.Console

object Injector {

    fun getPi4jConsole() = Console()

    fun getPi4j(): Context = Pi4J.newAutoContext()

    fun getRuntimeConfiguration(): RuntimeConfigurationWorker = RuntimeConfigurationWorkerImpl()

    fun getRuntimeAIConfiguration(): RuntimeAIConfigurationWorker = RuntimeAIConfigurationWorkerImpl()
}