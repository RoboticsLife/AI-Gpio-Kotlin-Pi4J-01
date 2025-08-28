package brain.perceptions.vision

import avatar.Avatar
import avatar.hardware.HardwareTypes
import avatar.hardware.types.circuitboard.data.BodyCircuitBoard
import brain.perceptions.vision.Vision.VisionTypes

class CameraVision(val avatar: Avatar) : Vision {



    override fun look(visionTypes: VisionTypes) {
        println("*********")
        (avatar.body.body as? BodyCircuitBoard)?.cameras?.first()?.recordPicture()

    /*    when(avatar.type) {
            HardwareTypes.Type.CIRCUIT_BOARD -> {
                avatar.body as BodyCircuitBoard

            }
            else -> {
                //TODO
            }
        }
      */
     //   if (avatar.type == HardwareTypes.Type.CIRCUIT_BOARD) {
      //      (avatar.body as BodyCircuitBoard).cameras.first().recordVideo()
       // }

    }


}