package spaceEngineers.game.mockable

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.*
import spaceEngineers.model.extensions.allBlocks
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PolymorphicBlocksTest : MockOrRealGameTest() {

    @Test
    fun smallGenerator() = testContext {
        buildAndCheckType<FueledPowerProducer>(DefinitionId.reactor("LargeBlockSmallGenerator"))
        buildAndCheckType<TerminalBlock>(DefinitionId.reactor("LargeBlockSmallGenerator"))
        buildAndCheckType<FunctionalBlock>(DefinitionId.reactor("LargeBlockSmallGenerator"))
    }

    @Test
    fun slideDoor() = testContext {
        buildAndCheckType<DoorBase>(DefinitionId.door("LargeBlockSlideDoor"))
        buildAndCheckType<TerminalBlock>(DefinitionId.door("LargeBlockSlideDoor"))
        buildAndCheckType<FunctionalBlock>(DefinitionId.door("LargeBlockSlideDoor"))
    }


    private inline fun <reified T> SpaceEngineers.buildAndCheckType(definitionId: DefinitionId) {
        val z = 1000
        admin.character.teleport(Vec3F(0, 0, z + 15), Vec3F.FORWARD, Vec3F.UP)
        observer.observeNewBlocks()
        admin.blocks.placeAt(definitionId, Vec3F(0, 0, z + 0), Vec3F.FORWARD, Vec3F.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.definitionId.type, definitionId.type)
        assertTrue(block is T)
        admin.blocks.remove(block.id)
    }
}
