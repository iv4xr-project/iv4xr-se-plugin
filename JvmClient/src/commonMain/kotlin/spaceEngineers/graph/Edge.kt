package spaceEngineers.graph

interface Edge<EdgeId, NodeId, EdgeData> {
    val id: EdgeId
    val from: NodeId
    val to: NodeId
    val data: EdgeData
}

data class DirectedEdge<EdgeId, NodeId, EdgeData>(
    override val from: NodeId,
    override val to: NodeId,
    override val id: EdgeId,
    override val data: EdgeData,
) : Edge<EdgeId, NodeId, EdgeData>
