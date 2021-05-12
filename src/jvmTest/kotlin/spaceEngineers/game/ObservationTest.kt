package spaceEngineers.game

import spaceEngineers.model.allBlocks
import testhelp.JSON_RESOURCES_DIR
import testhelp.MockOrRealGameTest
import testhelp.preferMocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class ObservationTest : MockOrRealGameTest(jsonMockFile = DEFINITIONS_FILE, useMock = preferMocking) {


    //@Test //uncomment to update definitions json file
    fun updateDefinitionsFile() = runAndWriteResponse {
        observer.observeBlocks()
    }


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
            assertNotNull(it.size)
        }
        assertEquals(
            2,
            observer.observeBlocks().allBlocks.map { it.orientationForward }.distinct().size
        )
        println(observer.observeBlocks().allBlocks.map { it.orientationForward }.distinct())
    }

    companion object {
        const val DEFINITIONS_JSON = "observation.json"
        val DEFINITIONS_FILE = File("${JSON_RESOURCES_DIR}${DEFINITIONS_JSON}")
    }

}
