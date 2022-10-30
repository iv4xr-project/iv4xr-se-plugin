package spaceEngineers.graph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Node<NodeId, NodeData> {
    val id: NodeId
    val data: NodeData
}

@Serializable
data class DataNode<NodeId, NodeData>(
    @SerialName("Id")
    override val id: NodeId,
    @SerialName("Position")
    override val data: NodeData,
) : Node<NodeId, NodeData>
