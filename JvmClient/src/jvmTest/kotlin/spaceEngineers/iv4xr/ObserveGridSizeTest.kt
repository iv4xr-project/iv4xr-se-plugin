package spaceEngineers.iv4xr

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveGridSizeTest : MockOrRealGameTest(
    inMockResourcesDirectory("ObserveGridSizeTest.txt") // scenarioId = "small",
    // forceRealGame = true,
    // loadScenario = true
) {

    // @Disabled("This test required a game instance running, enable manually by uncommenting.")
    @Test
    fun test_observed_grids() = testContext {
        assertEquals(observer.observeBlocks().grids.size, 1)
        assertEquals(observer.observeBlocks().grids.size, 1)
        assertEquals(observer.observeBlocks().grids.size, 1)
    }
}
