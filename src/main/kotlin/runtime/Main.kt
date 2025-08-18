package org.example.runtime

import avatar.Avatar
import avatar.hardware.AvatarBuilder
import avatar.hardware.HardwareTypes
import avatar.hardware.parts.basecomponents.DistanceSensor
import avatar.hardware.types.circuitboard.CircuitBoard
import brain.Brain
import brain.BrainBuilder
import brain.emitters.NetworkEmitters
import com.pi4j.context.Context
import com.pi4j.util.Console
import kotlinx.coroutines.*
import network.weatherservice.WeatherNetworkService
import brain.data.local.Configuration
import brain.emitters.DistanceEmitters
import brain.utils.toCm
import network.databases.DatabaseInitializer
import runtime.setup.Injector
import kotlin.math.abs


/**
 * LESSON 12: Firebase Database remote connection
 * GPIO-Kotlin-Pi4j project.
 * Kotlin Gpio project. Working with IO lines on Raspberry Pi using Pi4J Kotlin/Java langs and remote
 * compiling / debugging to any ARM GPIO compatible hardware. Advanced AI features (TensorFlow)
 */

//Hardware
lateinit var pi4j: Context
lateinit var console: Console
lateinit var configuration: Configuration
lateinit var avatar: Avatar
lateinit var brain: Brain
var city = "Toronto"

suspend fun main(args: Array<String>) {

    init()
    collectData()

    //Print out
    console.println(pi4j.boardInfo().boardModel)

    val weatherNetworkService = WeatherNetworkService()

    weatherNetworkService.getWeatherByName(city)

    //add infinite loop for java app running
    coroutineScope {
        println("Start infinite main thread")
        delay(Long.MAX_VALUE)
        println("End infinite main thread")
    }

}

fun init() {
    pi4j = Injector.getPi4j()
    console = Injector.getPi4jConsole()
    configuration = Injector.getRuntimeConfiguration().getConfiguration("lesson13-mpu6050-i2c.json")
    avatar = AvatarBuilder(pi4j, configuration).build()
    brain = BrainBuilder(avatar = avatar).build()
}

fun collectData() {

    val jobWeatherCollector = CoroutineScope(Job() + Dispatchers.IO).launch {
        NetworkEmitters.weatherEmitter.collect { weather ->
            if (weather.isSuccessful && weather.weatherResponse != null) {
                println(weather)

            }

        }
    }
}
