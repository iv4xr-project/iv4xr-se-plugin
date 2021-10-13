package spaceEngineers.game.mockable

import spaceEngineers.transport.SocketReaderWriter
import testhelp.MockOrRealGameTest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals


class BlockDefinitionHierarchyTest : MockOrRealGameTest(inMockResourcesDirectory("BlockDefinitionHierarchyTest.txt")) {

    @Test
    fun size() = testContext {
        definitions.blockDefinitionHierarchy().let { map ->
            assertEquals(85, map.size)
        }
    }


    @Test
    fun print() = testContext {
        definitions.blockDefinitionHierarchy().forEach { it ->
            println(it)
        }
    }


}
