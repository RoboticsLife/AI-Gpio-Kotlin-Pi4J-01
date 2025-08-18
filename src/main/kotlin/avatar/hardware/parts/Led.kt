package avatar.hardware.parts

import com.pi4j.context.Context
import com.pi4j.io.gpio.digital.DigitalOutput
import com.pi4j.io.gpio.digital.DigitalOutputProvider
import kotlinx.coroutines.Job
import brain.data.local.Configuration

class Led(pi4j: Context, ledConfig: Configuration.LedConfig) {

    private var ledOutput: DigitalOutput? = null
    private var name: String? = null
    var threadScope: Job? = null

    init {
        buildLed(pi4j, ledConfig)
    }

    private fun buildLed(pi4j: Context, ledConfig: Configuration.LedConfig) {
        try {
            ledOutput = pi4j.digitalOutput<DigitalOutputProvider>().create(ledConfig.pin)
            name = ledConfig.name ?: "LED"
        } catch (e: Exception) {
            //TODO emit error to a specific emitter (create it)
        }

    }

    fun on() {
        ledOutput?.high()
    }

    fun off() {
        ledOutput?.low()
    }

}