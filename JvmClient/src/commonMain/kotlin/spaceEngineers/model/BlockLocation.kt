package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockLocation(
    @SerialName("DefinitionId")
    val definitionId: DefinitionId,
    @SerialName("MinPosition")
    val minPosition: Vec3I,
    @SerialName("OrientationForward")
    val orientationForward: Vec3I = Vec3I.FORWARD,
    @SerialName("OrientationUp")
    val orientationUp: Vec3I = Vec3I.UP,
)
