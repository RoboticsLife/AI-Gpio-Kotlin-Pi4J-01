package brain.perceptions.speaking

import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem


object AudioSourceCreator {

    fun createAudioSourceFromDecodedString(base64AudioDataString: String) {
        //val audioBytes: ByteArray? = Base64.getDecoder().decode(base64AudioDataString)
        val audioBytes = base64AudioDataString.byteInputStream()

        //val outputFilePath = "outputaudio.wav"

        val sampleRate = 24000.0f
        val sampleSizeInBits = 16
        val channels = 1 // Assuming mono, as it's not specified
        val signed = true // PCM is almost always signed
        val bigEndian = true // A common default for audio/L16
        val audioFormat = AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian)

        val outputFilePath = "output.wav"

        try {
            if (base64AudioDataString != null) {
                convertBytesToWav(base64AudioDataString, audioFormat, outputFilePath);

                //    val path = this::class.java.filesDir.absolutePath

         //       Files.write(Paths.get(outputFilePath), audioBytes)
                println("File was created")
              //  this::class.java.res
            }

        } catch (e: Exception) {
            println(e.message)
        }
        //println(audioBytes)
    }


    @Throws(IOException::class)
   //////// fun convertBytesToWav(audioBytes: ByteArray, format: AudioFormat, outputPath: String?) {
    fun convertBytesToWav(audioBytes: String, format: AudioFormat, outputPath: String?) {
        // Create a ByteArrayInputStream from your audio bytes
      //////////  val bais = ByteArrayInputStream(audioBytes)
        val bais = audioBytes.byteInputStream()
       // val audioInputStreamNew = AudioSystem.getAudioInputStream(bais)
        val audioInputStreamNew = AudioSystem.getAudioInputStream(audioBytes.byteInputStream())
        println(audioInputStreamNew.format)


        // Create an AudioInputStream from the ByteArrayInputStream and the specified AudioFormat
        // The third argument (length) should be the number of frames, which can be calculated
        // based on the byte array length, frame size, and number of channels.
        // For simplicity, here we assume raw audio data without a WAV header.
        // If your byte array already includes a WAV header, you might be able to
        // directly use AudioSystem.getAudioInputStream(bais).
      //  val frames = (audioBytes.size / format.getFrameSize()).toLong()
      //  val audioInputStream = AudioInputStream(bais, format, frames)

        // Create a File object for the output WAV file
     //   val outputFile: File = File(outputPath)
        //println(outputFile)
        //println(outputPath)

      //  val source: Path = Paths.get(this.javaClass.getResource("/").getPath())
       // val newFolder = Paths.get(source.toAbsolutePath().toString() + "/newFolder/")
       // val outputFile: File = File(source)

     //   val url: URL? = this.javaClass.getResource("/")
       // val parentDirectory: File = File(URI(url.toString()))
       // val outputFile = File(parentDirectory, "output.wav")

        // Write the AudioInputStream to the file in WAV format
       // AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile)

        // Close the streams
    //    audioInputStream.close()
      //  bais.close()
    }
}