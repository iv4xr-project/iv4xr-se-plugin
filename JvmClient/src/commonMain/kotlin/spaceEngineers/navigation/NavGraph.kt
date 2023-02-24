package spaceEngineers.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import spaceEngineers.graph.DirectedGraph
import spaceEngineers.model.BlockId
import spaceEngineers.model.Vec3F

@Serializable
data class NavGraph(
    @SerialName("Nodes")
    val nodes: List<Node> = emptyList(),
    @SerialName("Edges")
    val edges: List<Edge> = emptyList()
) {
    fun toDirectedGraph(): DirectedGraph<BlockId, Vec3F, String, String> {
        return DirectedGraph(
            nodes = this.nodes,
            edges = this.edges,
        )
    }
}
