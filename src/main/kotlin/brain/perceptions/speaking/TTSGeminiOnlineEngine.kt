package brain.perceptions.speaking

import avatar.Avatar
import java.io.ByteArrayInputStream


class TTSGeminiOnlineEngine(avatar: Avatar): TTSVoice {


    override fun talk(byteArray: String) {
        speakStringToVoice(byteArray)
    }

    override fun speakStringToVoice(byteArray: String) {
        //TODO connect to gemini and speak
       // val a = AudioInputStream
        println("________________________________________________")
        println(byteArray)
        //AudioSourceCreator.createAudioSourceFromDecodedString(byteArray)

       // val bais = ByteArrayInputStream(audioBytes)

    }

}