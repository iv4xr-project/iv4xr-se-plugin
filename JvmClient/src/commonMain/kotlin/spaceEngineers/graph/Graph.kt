package spaceEngineers.graph

data class DirectedGraph<NodeId, NodeData, EdgeId, EdgeData>(
    val nodes: List<Node<NodeId, NodeData>>,
    val edges: List<DataEdge<EdgeId, NodeId, EdgeData>>
)

data class DirectedGraphExtra<NodeId, NodeData, EdgeId, EdgeData>(
    val graph: DirectedGraph<NodeId, NodeData, EdgeId, EdgeData>,
) {
    val nodesById: Map<NodeId, Node<NodeId, NodeData>> = graph.nodes.associateBy { it.id }
    val edgesByFrom: Map<NodeId, List<DataEdge<EdgeId, NodeId, EdgeData>>> = graph.nodes.map { node ->
        node.id to graph.edges.filter { edge -> edge.from == node.id }
    }.toMap()
}

fun <NodeId, NodeData, EdgeId, EdgeData> DirectedGraph<NodeId, NodeData, EdgeId, EdgeData>.toExtra(): DirectedGraphExtra<NodeId, NodeData, EdgeId, EdgeData> {
    return DirectedGraphExtra(this)
}
