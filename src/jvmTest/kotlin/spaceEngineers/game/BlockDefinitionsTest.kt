package spaceEngineers.game

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcCharacterController
import spaceEngineers.controller.loadFromTestResources
import testhelp.spaceEngineers
import kotlin.test.Test
import kotlin.test.assertEquals


class BlockDefinitionsTest {


    @Test
    fun getDefinition() = spaceEngineers {
        session.loadFromTestResources("simple-place-grind-torch")
        val ccw = this as ContextControllerWrapper
        val se = ccw.spaceEngineers as JsonRpcCharacterController

        se.blockDefinitions()
        assertEquals(626, se.blockDefinitions().size)
    }

}