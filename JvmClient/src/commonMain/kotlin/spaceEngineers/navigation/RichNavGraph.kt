package spaceEngineers.navigation

class RichNavGraph (
    val navGraph: NavGraph = NavGraph(),
) {
    val nodeMap : Map<NodeId, Node> = navGraph.nodes.associateBy { it.id }

    val edgeMap: Map<NodeId, Set<NodeId>> = navGraph.nodes.associateBy({ it.id }, { node ->
        navGraph.edges.filter { it.isConnectedTo(node.id) }.map { it.otherEnd(node.id) }.toSet()
    })

    private fun checkNodeId(id: NodeId) {
        if (!nodeMap.containsKey(id))
            throw NoSuchElementException("NodeId $id was not found in the map.")
    }

    fun node(id: NodeId): Node {
        checkNodeId(id)
        return nodeMap.getValue(id)
    }

    fun neighbours(id: NodeId): Set<NodeId> {
        checkNodeId(id)  // This is checking nodeMap, but it's virtually guaranteed to be equivalent.
        return edgeMap.getValue(id)
    }
    
    fun containsEdge(from: NodeId, to: NodeId): Boolean {
        return edgeMap[from]?.contains(to) ?: false
    }

    fun distance(from: NodeId, to: NodeId): Float {
        return node(from).position.distanceTo(node(to).position)
    }
}


fun NavGraph.toRichGraph(): RichNavGraph {
    return RichNavGraph(this)
}
