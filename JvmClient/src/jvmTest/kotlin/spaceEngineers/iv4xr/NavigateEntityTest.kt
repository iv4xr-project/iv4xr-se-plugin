package spaceEngineers.iv4xr

import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.iv4xr.navigation.NavigableSystem
import spaceEngineers.model.Vec3F
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertNotEquals

class NavigateEntityTest : MockOrRealGameTest(
    inMockResourcesDirectory("NavigateEntityTest.txt") // scenarioId = "small",
    // forceRealGame = true,
    // loadScenario = true
) {

    @Test
    fun navigateMaze() = testContext {
        val navigableSystem = NavigableSystem(this, observer)
        val blockPosition = navigableSystem.setDesiredBlockPosition("BlockCryoChamber")
        assertNotEquals(blockPosition, Vec3F(0, 0, 0))

        val closestDistance = 3f

        val navigableGraph = navigableSystem.getNavigableGraph()
        val navigablePath = navigableSystem.getClosestPathToDesiredBlock(closestDistance)

        navigableSystem.navigateGroundedPath(navigableGraph, navigablePath)

        val finalDistance = observer.distanceTo(blockPosition)
        println("finalDistance: $finalDistance")

        // Check the agent is near the desired block
        assert(finalDistance < closestDistance)
    }
}
