package spaceEngineers.game.mockable

import spaceEngineers.model.extensions.allBlocks
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ObservationTest : MockOrRealGameTest(mockFile = inMockResourcesDirectory("ObservationTest.txt")) {

    @Test
    fun allCount() = testContext {
        assertEquals(
            101,
            observer.observeBlocks().allBlocks.size
        )
    }

    @Test
    fun nullChecks() = testContext {
        observer.observeBlocks().allBlocks.forEach {
            assertNotNull(it.orientationForward)
            assertNotNull(it.orientationUp)
            assertNotNull(it.minPosition)
            assertNotNull(it.maxPosition)
            assertNotNull(it.gridPosition)
            assertNotNull(it.size)
        }
        assertEquals(
            2,
            observer.observeBlocks().allBlocks.map { it.orientationForward }.distinct().size
        )
    }
}
