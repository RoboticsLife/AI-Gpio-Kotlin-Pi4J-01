package org.example.runtime

import avatar.Avatar
import avatar.hardware.AvatarBuilder
import brain.Brain
import brain.BrainBuilder
import brain.ai.data.local.AITextRequestParams
import brain.emitters.NetworkEmitters
import com.pi4j.context.Context
import com.pi4j.util.Console
import kotlinx.coroutines.*
import brain.data.local.Configuration
import network.aiservice.ollama.data.OllamaGenerateResponse
import runtime.setup.Injector

/**
 * LESSON 1: AI integration
 */

//Hardware
lateinit var pi4j: Context
lateinit var console: Console
lateinit var configuration: Configuration
lateinit var avatar: Avatar
lateinit var brain: Brain

suspend fun main(args: Array<String>) {

    init()
    collectData()

    //Print out
    console.println(pi4j.boardInfo().boardModel)

    //brain.lookUseAI("what do you see on this picture?")
    brain.lookUseAI("what do you see on this picture?", params = AITextRequestParams(aiTextResponseLengthLimit = 10))

   // brain.askUseAI("How do you feel today?", params = AITextRequestParams(aiTextResponseLengthLimit = 20))

   // delay(3000)
  //  brain.askUseAI("What is the weather in toronto today?")
   // delay(5000)
   // brain.askUseAI("Does the Earth flat?", params = AITextRequestParams(aiTextResponseLengthLimit = 1))
   // delay(5000)

   // brain.askUseAI("Your favourite color?")
   // delay(5000)
   // brain.askUseAI("Do you like robotics?")



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
    configuration = Injector.getRuntimeConfiguration().getConfiguration("lesson01-ai.json")
    avatar = AvatarBuilder(pi4j, configuration).build()
    brain = BrainBuilder(avatar = avatar).build(BrainBuilder.Type.ROBOT)
}
 fun collectData() {
     val jobAICollector = CoroutineScope(Job() + Dispatchers.IO).launch {
         NetworkEmitters.aiEmitter.collect { ai ->
             println((ai?.aiRequestResponseLinkedHashSet?.lastOrNull()?.response as? OllamaGenerateResponse)?.response)
         }
     }
}
