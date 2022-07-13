package spaceEngineers.navigation

class RichNavGraph (
    val navGraph: NavGraph = NavGraph(),
) {
    val edgeMap: Map<NodeId, Set<NodeId>> = navGraph.nodes.associateBy({ it.id }, { node ->
        navGraph.edges.filter { it.isConnectedTo(node.id) }.map { it.otherEnd(node.id) }.toSet()
    })

}


fun NavGraph.toRichGraph(): RichNavGraph {
    return RichNavGraph(this)
}
