package spaceEngineers.game.mockable

import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.normalizeAsRun
import spaceEngineers.model.extensions.normalizeAsWalk
import spaceEngineers.navigation.Edge
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationMazeTest : MockOrRealGameTest(
    scenarioId = "amaze",
    forceRealGame = false,
    loadScenario = true,
) {
    @Test
    fun nonemptyNavGraph() = testContext {
        observer.navigationGraph().let {
            // These values depend on observation radius (TODO: add the API and set the radius here)
            assertEquals(111, it.nodes.size)
            assertEquals(110, it.edges.size)
        }
    }

    private fun Edge.otherId(id: Int): Int = if (i == id) j else i

    @Test
    fun goToNextNode() = testContext {
        val navGraph = observer.navigationGraph()
        var me = observer.observe()

        val nearestNode = navGraph.nodes.minByOrNull { it.position.distanceTo(me.position) }
            ?: error("Nearest node not found")
        assertEquals(0, nearestNode.id)

        val edge = navGraph.edges.find { it.i == nearestNode.id || it.j == nearestNode.id } ?: error("No edge found")
        val nextNode = navGraph.nodes.find { it.id == edge.otherId(nearestNode.id) } ?: error("Invalid edge!")

        println("My position    : ${me.position}")
        println("Target position: ${nextNode.position}")

        val direction = (nextNode.position - me.position).normalizeAsRun()

        // only change forward orientation
        admin.character.teleport(me.position, direction, me.orientationUp)

        for (i in 1..20) {
            println()
            me = observer.observe()
            val distance = (nextNode.position - me.position).length()
            println("distance: $distance")
            if (distance < 0.65)
                return@testContext

            character.moveAndRotate(Vec3F.FORWARD.normalizeAsWalk(), ticks = 10)
            delay(110)
        }

        assertTrue(false, "didn't reach the node position")
    }

}
