package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals


class BlockHierarchyTest : MockOrRealGameTest(inMockResourcesDirectory("BlockHierarchyTest.txt")) {

    @Test
    fun size() = testContext {
        assertEquals(109, definitions.blockHierarchy().size)
    }

    @Test
    fun terminalParentOfCube() = testContext {
        assertEquals("MyObjectBuilder_CubeBlock", definitions.blockHierarchy()["MyObjectBuilder_TerminalBlock"])
    }

    @Test
    fun functionalParentOfTerminal() = testContext {
        assertEquals("MyObjectBuilder_TerminalBlock", definitions.blockHierarchy()["MyObjectBuilder_FunctionalBlock"])
    }


}
