package spaceEngineers.graph

interface Node<NodeId, NodeData> {
    val id: NodeId
    val data: NodeData
}

data class DataNode<NodeId, NodeData>(
    override val id: NodeId,
    override val data: NodeData,
) : Node<NodeId, NodeData>
