package spaceEngineers.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Edge (
    @SerialName("I")
    val i: Int,
    @SerialName("J")
    val j: Int
)
