package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals


class AllDefinitionsTest : MockOrRealGameTest(mockFile = inMockResourcesDirectory("AllDefinitionsTest.txt")) {

    @Test
    fun allCount() = testContext {
        assertEquals(
            1636,
            definitions.allDefinitions().size
        )
    }

    @Test
    fun uniqueIdTypeCount() = testContext {
        assertEquals(
            1636,
            definitions.allDefinitions().map { "${it.definitionId}" }.distinct().size
        )
    }

    @Test
    fun uniqueTypeCount() = testContext {
        assertEquals(
            1589,
            definitions.allDefinitions().map { it.definitionId.type }.distinct().size
        )
    }

    @Test
    fun publicCount() = testContext {
        assertEquals(
            1314,
            definitions.allDefinitions().count { it.public }
        )
    }

    @Test
    fun enabledCount() = testContext {
        assertEquals(
            1636,
            definitions.allDefinitions().count { it.enabled }
        )
    }

    @Test
    fun inSurvivalCount() = testContext {
        assertEquals(
            1466,
            definitions.allDefinitions().count { it.availableInSurvival }
        )
    }

    @Test
    fun typeDuplicities() = testContext {
        assertEquals(
            34,
            definitions.allDefinitions().groupBy { it.definitionId.type }.map { it.key to it.value.size }
                .filter { it.second > 1 }
                .onEach(::println).size
        )
    }

    @Test
    fun allIds() = testContext {
        assertEquals(
            123,
            definitions.allDefinitions().map { it.definitionId.id }.distinct().onEach(::println).size
        )
    }

    @Test
    fun welderId() = testContext {
        val w = definitions.allDefinitions().find { it.definitionId.type == "Welder2Item" } ?: error("no welder found")
        assertEquals(
            "MyObjectBuilder_PhysicalGunObject",
            w.definitionId.id
        )
    }
}
