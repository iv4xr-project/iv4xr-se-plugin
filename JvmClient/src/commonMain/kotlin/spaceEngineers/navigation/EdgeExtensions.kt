package spaceEngineers.navigation

fun Edge.otherEnd(id: NodeId): NodeId {
    return when (id) {
        from -> to
        to -> from
        else -> error("The edge ($from, $to) doesn't contain id $id.")
    }
}

fun Edge.isConnectedTo(id: NodeId): Boolean {
    return (from == id) || (to == id)
}
