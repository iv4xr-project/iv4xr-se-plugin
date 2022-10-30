package spaceEngineers.game.mockable

import spaceEngineers.controller.Observer
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.controller.extensions.navigationGraph
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.blockById
import spaceEngineers.model.extensions.normalizeAsWalk
import spaceEngineers.navigation.Edge
import spaceEngineers.navigation.isConnectedTo
import spaceEngineers.navigation.otherEnd
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class NavigationMazeTest : MockOrRealGameTest(
    scenarioId = "amaze-20x20",
) {
    @Test
    fun nonemptyNavGraph() = testContext {
        with(observer.navigationGraph()) {
            // These values depend on observation radius (TODO: add the API and set the radius here)
            assertEquals(111, nodes.size)
            assertEquals(110, edges.size)
        }
    }

    @Test
    fun goToNextNode() = testContext {
        val navGraph = observer.navigationGraph()
        val me = observer.observe()

        val nearestNode = navGraph.nodes.minByOrNull { it.data.distanceTo(me.position) }
            ?: error("Nearest node not found")

        val nearestBlock = observer.observeBlocks().blockById(nearestNode.id)
        assertEquals(Vec3F(x=5.0, y=2.5, z=0.0), nearestBlock.position)

        val edge = navGraph.edges.find { it.isConnectedTo(nearestNode.id) } ?: error("No edge found")
        val nextNode = navGraph.nodes.find { it.id == edge.otherEnd(nearestNode.id) } ?: error("Invalid edge!")

        println("My position    : ${me.position}")
        println("Target position: ${nextNode.data}")

        val direction = (nextNode.data - me.position).normalized()
        admin.character.teleport(me.position, direction, me.orientationUp)  // Only change the forward orientation.

        goToPosition(this, nextNode.data, this@NavigationMazeTest)
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
