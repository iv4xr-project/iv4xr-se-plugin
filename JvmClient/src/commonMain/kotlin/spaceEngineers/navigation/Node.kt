package spaceEngineers.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import spaceEngineers.model.Vec3F

@Serializable
data class Node (
    @SerialName("Id")
    val id: Int,
    @SerialName("Position")
    val position: Vec3F
)
