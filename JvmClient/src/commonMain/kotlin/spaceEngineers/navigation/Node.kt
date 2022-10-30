package spaceEngineers.navigation

import eu.iv4xr.framework.spatial.Vec3
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import spaceEngineers.graph.DataNode
import spaceEngineers.model.Vec3F

typealias NodeId = String
typealias Node = DataNode<NodeId, Vec3F>

