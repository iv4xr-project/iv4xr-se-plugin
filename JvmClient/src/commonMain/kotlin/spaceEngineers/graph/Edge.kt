package spaceEngineers.graph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Edge<EdgeId, NodeId, EdgeData> {
    val id: EdgeId
    val from: NodeId
    val to: NodeId
    val data: EdgeData
}

@Serializable
data class DataEdge<EdgeId, NodeId, EdgeData>(
    @SerialName("I")
    override val from: NodeId,
    @SerialName("J")
    override val to: NodeId,
    @SerialName("Id")
    override val id: EdgeId,
    @SerialName("Data")
    override val data: EdgeData,
) : Edge<EdgeId, NodeId, EdgeData>
