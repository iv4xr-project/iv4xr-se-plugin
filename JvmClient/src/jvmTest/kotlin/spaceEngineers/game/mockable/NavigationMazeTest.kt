package spaceEngineers.game.mockable

import spaceEngineers.controller.Observer
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.controller.extensions.navigationGraph
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.normalizeAsWalk
import spaceEngineers.navigation.Edge
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class NavigationMazeTest : MockOrRealGameTest(
    scenarioId = "amaze",
    forceRealGame = false,
    loadScenario = true,
) {
    @Test
    fun nonemptyNavGraph() = testContext {
        with(observer.navigationGraph()) {
            // These values depend on observation radius (TODO: add the API and set the radius here)
            assertEquals(111, nodes.size)
            assertEquals(110, edges.size)
        }
    }

    private fun Edge.otherEnd(id: Int): Int {
        return when(id) {
            i -> j
            j -> i
            else -> error("The edge doesn't contain id $id.")
        }
    }

    @Test
    fun goToNextNode() = testContext {
        val navGraph = observer.navigationGraph()
        val me = observer.observe()

        val nearestNode = navGraph.nodes.minByOrNull { it.position.distanceTo(me.position) }
            ?: error("Nearest node not found")
        assertEquals(0, nearestNode.id)

        val edge = navGraph.edges.find { it.i == nearestNode.id || it.j == nearestNode.id } ?: error("No edge found")
        val nextNode = navGraph.nodes.find { it.id == edge.otherEnd(nearestNode.id) } ?: error("Invalid edge!")

        println("My position    : ${me.position}")
        println("Target position: ${nextNode.position}")

        val direction = (nextNode.position - me.position).normalized()
        admin.character.teleport(me.position, direction, me.orientationUp)  // Only change the forward orientation.

        goToPosition(this, nextNode.position, this@NavigationMazeTest)
    }

    private suspend fun goToPosition(
        spaceEngineers: SpaceEngineers,
        targetPosition: Vec3F,
        test: NavigationMazeTest
    ) {
        repeat (20) {
            if (areWeThereYet(spaceEngineers.observer, targetPosition))
                return

            spaceEngineers.character.moveAndRotate(Vec3F.FORWARD.normalizeAsWalk(), ticks = 10)
            test.delay(120)
        }

        fail("Didn't reach the target position.")
    }

    private fun areWeThereYet(
        observer: Observer,
        targetPosition: Vec3F,
        tolerance: Float = 0.65f
    ): Boolean {
        val distance = observer.distanceTo(targetPosition)
        println("distance: $distance")
        return distance < tolerance
    }

}
