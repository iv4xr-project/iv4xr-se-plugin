package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
}
