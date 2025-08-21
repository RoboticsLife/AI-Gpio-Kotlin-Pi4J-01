package brain.perceptions

import brain.perceptions.vision.CameraVision
import brain.perceptions.vision.Vision

class RobotPerceptions: Perceptions {

    override var vision: Vision = CameraVision()

}