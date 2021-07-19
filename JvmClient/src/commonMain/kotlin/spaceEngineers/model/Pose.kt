package spaceEngineers.model

import kotlinx.serialization.SerialName


interface Pose {
    @SerialName("Position")
    val position: Vec3
    @SerialName("OrientationForward")
    val orientationForward: Vec3
    @SerialName("OrientationUp")
    val orientationUp: Vec3
}
