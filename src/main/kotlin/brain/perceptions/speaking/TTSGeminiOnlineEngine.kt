package brain.perceptions.speaking

import java.io.ByteArrayInputStream


class TTSGeminiOnlineEngine: TTSVoice {


    override fun talk(byteArray: String) {
        speakStringToVoice(byteArray)
    }

    override fun speakStringToVoice(byteArray: String) {
        //TODO connect to gemini and speak
       // val a = AudioInputStream
        println("________________________________________________")
        println(byteArray)

       // val bais = ByteArrayInputStream(audioBytes)

    }

}