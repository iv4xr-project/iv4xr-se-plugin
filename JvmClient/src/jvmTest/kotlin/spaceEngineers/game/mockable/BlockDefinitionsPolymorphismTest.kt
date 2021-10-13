package spaceEngineers.game.mockable

import spaceEngineers.model.AirtightDoorGenericDefinition
import spaceEngineers.model.LCDPanelsBlockDefinition
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
    fun slideDoor() = testContext {
        val door = definitions.blockDefinitions().first {
            it.type == "MyAirtightSlideDoorDefinition"
        }
        assertTrue(door is AirtightDoorGenericDefinition)
        assertEquals(door.powerConsumptionIdle, 0.00001f)
        assertEquals(door.powerConsumptionMoving, 0.001f)
        assertEquals(door.openingSpeed, 1.4f)
    }

    @Test
    fun hangarDoor() = testContext {
        val door = definitions.blockDefinitions().first {
            it.type == "MyAirtightHangarDoorDefinition"
        }
        assertTrue(door is AirtightDoorGenericDefinition)
        assertEquals(door.powerConsumptionIdle, 0.00001f)
        assertEquals(door.powerConsumptionMoving, 0.001f)
        assertEquals(door.openingSpeed, 0.1f)
    }

    @Test
    fun lcd() = testContext {
        val door = definitions.blockDefinitions().first {
            it.type == "MyLCDPanelsBlockDefinition"
        }
        assertTrue(door is LCDPanelsBlockDefinition)
        assertEquals(door.requiredPowerInput, 0.0001f)
    }
}
