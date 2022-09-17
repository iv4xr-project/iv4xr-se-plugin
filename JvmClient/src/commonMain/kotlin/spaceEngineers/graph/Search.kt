package spaceEngineers.graph

typealias Path<EdgeId> = List<EdgeId>

interface GraphSearch<NodeId, EdgeId> {
    fun findPath(from: NodeId, to: NodeId): Path<EdgeId>
}

class BasicGraphSearch<NodeId, NodeData, EdgeId, EdgeData>(
    val graphExtra: DirectedGraphExtra<NodeId, NodeData, EdgeId, EdgeData>,
    val canNavigateToSelf: Boolean = true,
) :
    GraphSearch<NodeId, EdgeId> {
    data class SearchContext<NodeId, EdgeId>(
        val finishedNodes: MutableSet<NodeId>,
        val currentPath: List<EdgeId>,
        val destination: NodeId,
    ) {
        fun withAddedEdge(edge: EdgeId): SearchContext<NodeId, EdgeId> {
            return copy(currentPath = currentPath.toMutableList().apply {
                add(edge)
            }.toList())
        }
    }

    override fun findPath(from: NodeId, to: NodeId): Path<EdgeId> {
        check(from in graphExtra.graph.nodes.map { it.id }) {
            "From $from not in the navigation graph, cannot find path."
        }
        check(to in graphExtra.graph.nodes.map { it.id }) {
            "To $to not in the navigation graph, cannot find path."
        }
        if (canNavigateToSelf && from == to) {
            return emptyList()
        }
        return visitNode(from, SearchContext<NodeId, EdgeId>(mutableSetOf(), listOf(), destination = to))
            ?: error("Path from $from to $to not found")
    }


    private fun visitNode(nodeId: NodeId, searchContext: SearchContext<NodeId, EdgeId>): Path<EdgeId>? {
        if (nodeId in searchContext.finishedNodes) {
            return null
        }
        searchContext.finishedNodes.add(nodeId)
        val edges = graphExtra.edgesByFrom[nodeId] ?: return null
        if (edges.isEmpty()) {
            return null
        }
        val destinationEdge = edges.firstOrNull { it.to == searchContext.destination }
        if (destinationEdge != null) {
            return searchContext.currentPath + listOf(destinationEdge.id)
        }
        edges.forEach {
            val visit = visitNode(it.to, searchContext.withAddedEdge(it.id))
            if (visit != null) {
                return visit
            }
        }
        return null
    }
}
