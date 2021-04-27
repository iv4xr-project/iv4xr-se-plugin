package spaceEngineers.game

import testhelp.JSON_RESOURCES_DIR
import testhelp.MockOrRealGameTest
import testhelp.preferMocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals


class AllDefinitionsTest : MockOrRealGameTest(jsonMockFile = DEFINITIONS_FILE, useMock = preferMocking) {


    //@Test //uncomment to update definitions json file
    fun updateDefinitionsFile() = runAndWriteResponse {
        definitions.allDefinitions()
    }


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
            definitions.allDefinitions().map { "${it.id}${it.blockType}" }.distinct().size
        )
    }

    @Test
    fun uniqueTypeCount() = testContext {
        assertEquals(
            1589,
            definitions.allDefinitions().map { it.blockType }.distinct().size
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
            definitions.allDefinitions().groupBy { it.blockType }.map { it.key to it.value.size }
                .filter { it.second > 1 }
                .onEach(::println).size
        )
    }

    @Test
    fun allIds() = testContext {
        assertEquals(
            123,
            definitions.allDefinitions().map { it.id }.distinct().onEach(::println).size
        )
    }

    @Test
    fun welderId() = testContext {
        val w = definitions.allDefinitions().find { it.blockType == "Welder2Item" } ?: error("no welder found")
        assertEquals(
            "MyObjectBuilder_PhysicalGunObject",
            w.id
        )
    }

    companion object {
        const val DEFINITIONS_JSON = "all_definitions.json"
        val DEFINITIONS_FILE = File("${JSON_RESOURCES_DIR}${DEFINITIONS_JSON}")
    }

}
