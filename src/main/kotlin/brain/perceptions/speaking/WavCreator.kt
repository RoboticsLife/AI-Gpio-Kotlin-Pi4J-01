package brain.perceptions.speaking

import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.math.sin

object WavCreator {
    @JvmStatic
    fun main(args: Array<String>) {
        // --- Step 1: Define audio parameters from your MIME string ---
        // "audio/L16;codec=pcm;rate=24000"
        val sampleRate = 24000.0f
        val sampleSizeInBits = 16
        val channels = 1 // Assuming mono, as it's not specified
        val signed = true // PCM is almost always signed
        val bigEndian = true // A common default for audio/L16

        // --- Step 2: Get your raw audio bytes from the API response ---
        // For this example, we'll generate a 1-second, 440Hz sine wave
        // to simulate the data you would receive.
        //TODO HERE IS THE MOCKUP. REPLACE IT WITH String from API response
        val audioBytes = generateSineWave(440.0, 1.0, sampleRate)

        // --- Step 3: Create an AudioFormat object with these parameters ---
        val audioFormat = AudioFormat(
            sampleRate,
            sampleSizeInBits,
            channels,
            signed,
            bigEndian
        )

        // --- Step 4: Create an AudioInputStream ---
        // This stream combines the raw bytes with their format description.
        val byteInputStream = ByteArrayInputStream(audioBytes)
        val audioInputStream = AudioInputStream(
            byteInputStream,
            audioFormat,
            (audioBytes.size / audioFormat.getFrameSize()).toLong() // Total frames
        )

        // --- Step 5: Write the AudioInputStream to a .wav file ---
        val outputFile = File("output.wav")
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile)
            println("✅ Successfully created audio file at: " + outputFile.getAbsolutePath())
        } catch (e: IOException) {
            System.err.println("Error writing audio file: " + e.message)
            e.printStackTrace()
        }
    }

    /**
     * Generates raw PCM bytes for a sine wave to simulate API data.
     * @param frequency The frequency of the tone (e.g., 440 Hz for 'A').
     * @param durationSeconds The duration of the sound.
     * @param sampleRate The sample rate.
     * @return A byte array containing the raw 16-bit PCM data.
     */
    fun generateSineWave(frequency: Double, durationSeconds: Double, sampleRate: Float): ByteArray {
        val numSamples = (durationSeconds * sampleRate).toInt()
        val buffer = ByteArray(numSamples * 2) // 2 bytes per 16-bit sample

        for (i in 0..<numSamples) {
            val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
            // Calculate sample value and scale it to the 16-bit range
            val sample = (sin(angle) * Short.Companion.MAX_VALUE).toInt().toShort()

            // Write the 16-bit sample as two bytes (big-endian)
            buffer[2 * i] = (sample.toInt() shr 8).toByte()
            buffer[2 * i + 1] = (sample.toInt() and 0xFF).toByte()
        }
        return buffer
    }
}