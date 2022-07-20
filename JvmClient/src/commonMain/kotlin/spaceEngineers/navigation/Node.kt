package spaceEngineers.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import spaceEngineers.model.Vec3F

typealias NodeId = Int

@Serializable
data class Node (
    @SerialName("Id")
    val id: NodeId,
    @SerialName("Position")
    val position: Vec3F
)
