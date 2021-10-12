package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals


class BlockDefinitionHierarchyTest : MockOrRealGameTest(inMockResourcesDirectory("BlockDefinitionHierarchyTest.txt")) {

    @Test
    fun size() = testContext {
        assertEquals(85, definitions.blockDefinitionHierarchy().size)
    }


    @Test
    fun print() = testContext {
        definitions.blockDefinitionHierarchy().forEach { it ->
            println(it)
        }
    }


}
