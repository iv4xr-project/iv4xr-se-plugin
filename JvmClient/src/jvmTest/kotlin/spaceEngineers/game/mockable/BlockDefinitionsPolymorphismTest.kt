package spaceEngineers.game.mockable

import spaceEngineers.model.AirtightDoorGenericDefinition
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class BlockDefinitionsPolymorphismTest : MockOrRealGameTest(
    inMockResourcesDirectory("BlockDefinitionsPolymorphismTest.txt"),
    //forceRealGame = true,
    //loadScenario = false
) {

    @Test
    fun lcd() = testContext {
        val door = definitions.blockDefinitions().first {
            it.type == "MyAirtightSlideDoorDefinition"
        }
        assertTrue(door is AirtightDoorGenericDefinition)
        assertEquals(door.powerConsumptionIdle, 0.00001f)
    }
}
