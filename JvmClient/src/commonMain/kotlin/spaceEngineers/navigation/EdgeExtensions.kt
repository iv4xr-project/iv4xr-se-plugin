package spaceEngineers.navigation

fun Edge.otherEnd(id: Int): Int {
    return when(id) {
        i -> j
        j -> i
        else -> error("The edge ($i, $j) doesn't contain id $id.")
    }
}

fun Edge.isConnectedTo(id: Int): Boolean {
    return (i == id) || (j == id)
}
