package avatar.hardware.parts.camera

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDateTime

/**
 * FHNW implementation of a camera, works with the raspberry-pi v2 and v3 camera module and
 * the Pi4J-Basic-OS image on the raspberry-pi.
 *
 *
 * Maybe works on other camera-modules too, but is not yet tested.
 *
 *
 * It uses the rpicam-still and rpicam-vid bash commands. Those are pre-installed
 * on all raspbian-versions from Bookworm on.
 */
class Camera : Component() {
    /**
     * Constructor for using the picture and video functionality
     * calling the init function to test if a camera is active
     */
    init {
        init()
    }

    /**
     * Takes a picture using the bash commands
     *
     * @param config Use the ConfigBuilder of the CameraConfig to create the desired parameters
     */
    /**
     * Takes a picture and saves it to the default Pictures folder
     *
     *
     * If a file already exists, the code will break. better use useDate while taking pictures
     */
    @JvmOverloads
    fun recordPicture(
        config: PicConfig = newPictureConfigBuilder()
          //  .outputPath("/home/alik/Pictures/picam.jpg")
            .outputPath("/picam.jpg")
            .build()
    ) {
        println("Trying to make a picture!!!")

        logDebug("Taking Picture")

        val processBuilder = ProcessBuilder()
        processBuilder.command("bash", "-c", config.asCommand())

        try {
            callBash(processBuilder)
        } catch (e: Exception) {
            logException("Camera: Error while taking picture: ", e)
        }
    }

    /**
     * Takes a video with the configuration and saves it to the output path
     *
     * @param config path to the .h264 file
     */
    /**
     * Takes a video and saves it to the default Videos folder
     *
     *
     * If a file already exists, the code will break. better use useDate while taking videos
     */
    @JvmOverloads
    fun recordVideo(
        config: VidConfig = newVidConfigBuilder()
            .outputPath("/home/alik/Videos/video.h264")
            .recordTime(5000)
            .build()
    ) {
        logDebug("Taking Video")

        val processBuilder = ProcessBuilder()
        processBuilder.command("bash", "-c", config.asCommand())

        try {
            callBash(processBuilder)
        } catch (e: Exception) {
            logException("Camera: Error while taking video: ", e)
        }
    }

    /**
     * Uses a ProcessBuilder to call the bash of the RaspberryPI.
     * This will call the command and write the output to the console
     *
     * @param processBuilder which process needs to be built
     */
    @Throws(IOException::class, InterruptedException::class)
    private fun callBash(processBuilder: ProcessBuilder) {
        val process = processBuilder.start()

        val reader = BufferedReader(InputStreamReader(process.getInputStream()))

        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            println(line)
        }

        //exitCode 0 = No Errors
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            logError("Camera exited with error code : %s", exitCode)
        } else {
            logInfo("Camera finished successfully")
        }
    }

    /**
     * testing, if camera is installed on raspberrypi, and if the bash commands
     * will work
     */
    private fun init() {
        logDebug("initialisation of camera")

        val processBuilder = ProcessBuilder()
        processBuilder.command("bash", "-c", "rpicam-still")

        try {
            callBash(processBuilder)
        } catch (e: Exception) {
            logException("Camera: Error at initialisation: ", e)
        }
    }

    /**
     * Output Format of pictures
     * These modes determine the output of the picture-file
     *
     *
     * The following encodings can be set
     * [.PNG]
     * [.JPG]
     * [.RGB]
     * [.BMP]
     * [.YUV420]
     */
    enum class PicEncoding(encoding: String) {
        PNG("png"),
        JPG("jpg"),
        RGB("rgb"),
        BMP("bmp"),
        YUV420("yuv420");

        val encoding: String?

        init {
            this.encoding = encoding
        }
    }

    /**
     * Output Format of videos
     * These modes determine the output of the video-file
     *
     *
     * The following encodings can be set
     * [.H264]
     * [.MJPEG]
     * [.YUV420]
     */
    enum class VidEncoding(encoding: String) {
        H264("h264"),
        MJPEG("mjpeg"),
        YUV420("yuv420");

        val encoding: String?

        init {
            this.encoding = encoding
        }
    }

    /**
     * Builder Pattern to create a config for a single Picture
     */
    class PicConfig private constructor(builder: Builder) {
        /**
         * where should it be saved and what's the filename?
         */
        val outputPath: String?

        /**
         * using datetime as filename?
         * if yes, then the outputPath should be a path, not a file
         */
        val useDate: Boolean

        /**
         * a delay, before taking a picture
         */
        val delay: Int

        /**
         * output width of the picture
         */
        val width: Int

        /**
         * output height of the picture
         */
        val height: Int

        /**
         * the quality of the picture, ranging from 0 to 100
         * where 100 is the best quality of the picture, with no blurring
         */
        val quality: Int

        /**
         * The format of the output
         */
        val encoding: PicEncoding?

        /**
         * when true, there is no preview on the raspberry-pi
         */
        val disablePreview: Boolean

        /**
         * when true, the preview is in fullscreen
         */
        val allowFullscreenPreview: Boolean

        /**
         * constructor for the config
         *
         * @param builder builder with the defined options
         */
        init {
            this.outputPath = builder.outputPath
            this.useDate = builder.useDate
            this.delay = builder.delay
            this.width = builder.width
            this.height = builder.height
            this.quality = builder.quality
            this.encoding = builder.encoding
            this.disablePreview = builder.disablePreview
            this.allowFullscreenPreview = builder.allowFullscreenPreview
        }

        /**
         * Creates a callable bash command with the defined options.
         *
         * @return a string that can be called from the bash
         */
        fun asCommand(): String {
            val command = StringBuilder("rpicam-still")
            if (useDate) {
                command.append(" -o '").append(outputPath).append(LocalDateTime.now()).append(".")
                    .append(if (encoding != null) encoding else "jpg").append("'")
            } else {
                command.append(" -o '").append(outputPath).append("'")
            }
            if (delay != 0) {
                command.append(" -t ").append(delay)
            }
            if (width != 0) {
                command.append(" --width ").append(width)
            }
            if (height != 0) {
                command.append(" --height ").append(height)
            }
            if (quality != 0) {
                command.append(" -q ").append(quality)
            }
            if (encoding != null) {
                command.append(" --encoding ").append(encoding.encoding)
            }
            if (disablePreview) {
                command.append(" -n")
            }
            if (allowFullscreenPreview && !disablePreview) {
                command.append(" -f")
            }

            return command.toString()
        }

        /**
         * Builder Pattern, to create a config for a single picture
         *
         *
         * A Config is buildable like this:
         * var config = Camera.PicConfig.Builder.newInstance()
         * .outputPath("/home/pi/Pictures/")
         * .delay(3000)
         * .disablePreview(true)
         * .encoding(Camera.PicEncoding.PNG)
         * .useDate(true)
         * .quality(93)
         * .width(1280)
         * .height(800)
         * .build();
         *
         *
         * Every property can be added or not.
         */
        class Builder {
            var outputPath: String? = "/picam.jpg"
            var useDate = false
            var delay = 0
            var width = 1920
            var height = 1080
            var quality = 10
            var encoding: PicEncoding? = PicEncoding.JPG
            var disablePreview = false
            var allowFullscreenPreview = false

            fun outputPath(outputPath: String?): Builder {
                this.outputPath = outputPath
                return this
            }

            fun useDate(useDate: Boolean): Builder {
                this.useDate = useDate
                return this
            }

            fun delay(delay: Int): Builder {
                this.delay = delay
                return this
            }

            fun width(width: Int): Builder {
                this.width = width
                return this
            }

            fun height(height: Int): Builder {
                this.height = height
                return this
            }

            fun quality(quality: Int): Builder {
                require(!(quality < 0 || quality > 100)) { "quality must be between 0 and 100" }
                this.quality = quality
                return this
            }

            fun encoding(encoding: PicEncoding?): Builder {
                this.encoding = encoding
                return this
            }

            fun disablePreview(disablePreview: Boolean): Builder {
                this.disablePreview = disablePreview
                return this
            }

            fun allowFullscreenPreview(allowFullscreenPreview: Boolean): Builder {
                this.allowFullscreenPreview = allowFullscreenPreview
                return this
            }

            fun build(): PicConfig {
                return PicConfig(this)
            }
        }
    }

    /**
     * Builder Pattern to create a config for a video
     */
    class VidConfig private constructor(builder: Builder) {
        /**
         * where should it be saved and what's the filename?
         */
        val outputPath: String?

        /**
         * using datetime as filename?
         * if yes, then the outputPath should be a path, not a file
         */
        val useDate: Boolean

        /**
         * the length in milliseconds, how long the camera is actively filming
         */
        val recordTime: Int

        /**
         * the output-format of the video-file
         */
        val encoding: VidEncoding?

        /**
         * constructor for the config
         *
         * @param builder builder with the defined options
         */
        init {
            this.outputPath = builder.outputPath
            this.recordTime = builder.recordTime
            this.encoding = builder.encoding
            this.useDate = builder.useDate
        }

        /**
         * Creates a callable bash command with the defined options.
         *
         * @return a string that can be called from the bash
         */
        fun asCommand(): String {
            val command = StringBuilder("rpicam-vid -t " + recordTime)
            if (useDate) {
                command.append(" -o '").append(outputPath).append(LocalDateTime.now()).append(".")
                    .append(if (encoding != null) encoding else "h264").append("'")
            } else {
                command.append(" -o '").append(outputPath).append("'")
            }
            if (encoding != null) {
                command.append(" --codec ").append(encoding.encoding)
            }
            return command.toString()
        }

        /**
         * Builder Pattern, to create a config for a video
         *
         *
         * A Config is buildable like this:
         * var vidconfig = Camera.VidConfig.Builder.newInstance()
         * .outputPath("/home/pi/Videos/")
         * .recordTime(3000)
         * .useDate(true)
         * .build();
         *
         *
         * Every Property can be added or not.
         */
        class Builder {
            var outputPath: String? = null
            var recordTime = 0
            var encoding: VidEncoding? = null
            var useDate = false

            fun outputPath(outputPath: String?): Builder {
                this.outputPath = outputPath
                return this
            }

            fun recordTime(recordTime: Int): Builder {
                this.recordTime = recordTime
                return this
            }

            fun encoding(encoding: VidEncoding?): Builder {
                this.encoding = encoding
                return this
            }

            fun useDate(useDate: Boolean): Builder {
                this.useDate = useDate
                return this
            }

            fun build(): VidConfig {
                return VidConfig(this)
            }
        }
    }

    companion object {
        fun newPictureConfigBuilder(): PicConfig.Builder {
            return PicConfig.Builder()
        }

        fun newVidConfigBuilder(): VidConfig.Builder {
            return VidConfig.Builder()
        }
    }
}