package spaceEngineers.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NavGraph(
    @SerialName("Nodes")
    val nodes: List<Node> = emptyList(),
    @SerialName("Edges")
    val edges: List<Edge> = emptyList()
)
