package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationGraphTest : MockOrRealGameTest(
    mockFile = inMockResourcesDirectory("NavigationGraphTest.txt")
) {

    @Test
    fun nonemptyNavGraph() = testContext {
        observer.navigationGraph().let {
            assertEquals(40, it.nodes.size)
            assertEquals(67, it.edges.size)
        }
    }

    @Test
    fun navGraphConsistencyCheck() = testContext {
        val navGraph = observer.navigationGraph()
        val minId = navGraph.nodes.minOf { n -> n.id }
        val maxId = navGraph.nodes.maxOf { n -> n.id }
        assertTrue { minId < maxId }

        navGraph.edges.forEach {
            assertTrue { it.i < it.j }
            assertTrue { it.i >= minId }
            assertTrue { it.j <= maxId }
        }
    }
}
