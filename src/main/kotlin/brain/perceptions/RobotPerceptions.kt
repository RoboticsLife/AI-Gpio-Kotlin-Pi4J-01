package brain.perceptions

import avatar.Avatar
import brain.perceptions.vision.CameraVision
import brain.perceptions.vision.Vision

class RobotPerceptions(avatar: Avatar): Perceptions {

    override var vision: Vision = CameraVision(avatar)

}