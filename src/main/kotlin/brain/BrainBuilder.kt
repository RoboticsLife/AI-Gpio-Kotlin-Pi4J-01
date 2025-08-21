package brain

import avatar.Avatar

class BrainBuilder(private val avatar: Avatar) {

    fun build(type: Type = Type.ROBOT): Brain {
        return when(type) {
            Type.ROBOT -> RobotBrain().build(avatar)
            Type.HUMANOID -> RobotBrain().build(avatar) //TODO add more implementations
        }
    }

    enum class Type {
        ROBOT, HUMANOID,
    }

}