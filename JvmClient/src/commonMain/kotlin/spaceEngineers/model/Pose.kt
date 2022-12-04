package spaceEngineers.model

import kotlinx.serialization.SerialName

interface Pose {
    @SerialName("Position")
    val position: Vec3F
    @SerialName("OrientationForward")
    val orientationForward: Vec3F
    @SerialName("OrientationUp")
    val orientationUp: Vec3F
}
