package spaceEngineers.iv4xr

import org.junit.jupiter.api.Disabled
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigateEntityTest : MockOrRealGameTest(
        scenarioId = "simple-place-grind-torch",
        forceRealGame = true,
        loadScenario = true
) {

    @Disabled("This test required a game instance running, enable manually by uncommenting.")
    @Test
    fun test_observed_grids() = testContext {
        // I expect to obtain the existing grids in each executed observation
        // But it seems that every observation is returning current grid + last observed grid

        val firstObservedGrids = observer.observeBlocks().grids
        println("firstObservedGrids.size: " + firstObservedGrids.size)
        val secondObservedGrids = observer.observeBlocks().grids
        println("secondObservedGrids.size: " + secondObservedGrids.size)
        val thirdObservedGrids = observer.observeBlocks().grids
        println("thirdObservedGrids.size: " + thirdObservedGrids.size)

        assertEquals(firstObservedGrids.size, 1)
        assertEquals(secondObservedGrids.size, 1)
        assertEquals(thirdObservedGrids.size, 1)
    }

}