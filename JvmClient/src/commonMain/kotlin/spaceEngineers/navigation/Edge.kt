package spaceEngineers.navigation

import spaceEngineers.graph.DataEdge

typealias Edge = DataEdge<String, NodeId, String>


fun makeEdgeId(from: Any, to: Any): String {
    return "Edge-${from}-${to}"
}
