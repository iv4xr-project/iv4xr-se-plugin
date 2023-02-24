package spaceEngineers.graph

import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.neighbourPositions

data class Cube3dGraph<T : Any>(
    val blocksByPositions: Map<Vec3I, T>,
) {
    val positions = blocksByPositions.keys

    fun neighbourIds(node: Vec3I): Set<Vec3I> {
        return node.neighbourPositions().intersect(positions)
    }

    fun bfs(
        start: Vec3I,
        visited: Set<Vec3I> = emptySet(),
    ): Sequence<Pair<Vec3I, T>> {
        return explore(start = start, removeFunction = MutableList<Vec3I>::removeFirst, visited = visited)
    }

    fun dfs(
        start: Vec3I,
        visited: Set<Vec3I> = emptySet(),
    ): Sequence<Pair<Vec3I, T>> {
        return explore(start = start, removeFunction = MutableList<Vec3I>::removeLast, visited = visited)
    }

    private fun explore(
        start: Vec3I,
        removeFunction: (MutableList<Vec3I>.() -> Vec3I) = MutableList<Vec3I>::removeFirst,
        visited: Set<Vec3I> = emptySet(),
    ) = sequence<Pair<Vec3I, T>> {
        val currentlyVisited = visited.toMutableSet()
        val neighboursToExplore = mutableListOf(start)

        var position: Vec3I
        while (neighboursToExplore.isNotEmpty()) {
            position = removeFunction(neighboursToExplore)
            yield(position to blocksByPositions.getValue(position))
            currentlyVisited.add(position)
            neighboursToExplore.addAll(neighbourIds(position))
            neighboursToExplore.removeAll(currentlyVisited)
        }
    }
}
