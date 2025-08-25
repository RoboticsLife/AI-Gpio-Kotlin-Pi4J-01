package brain.perceptions.speaking

interface TTSVoice: Speak {

    fun speakStringToVoice(byteArray: String)
}