package brain.perceptions.vision

interface Vision {

    fun look(visionTypes: VisionTypes = VisionTypes.STATIC_IMAGE)

    enum class VisionTypes {
        STATIC_IMAGE,
        STATIC_IMAGE_WITH_FOCUSED_DIRECTION,
        VIDEO,
        VIDEO_WITH_FOCUSED_DIRECTION,
    }

}