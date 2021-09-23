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
        buildAndCheckType<FueledPowerProducer>("LargeBlockSmallGenerator")
        buildAndCheckType<TerminalBlock>("LargeBlockSmallGenerator")
        buildAndCheckType<FunctionalBlock>("LargeBlockSmallGenerator")
    }

    @Test
    fun slideDoor() = testContext {
        buildAndCheckType<DoorBase>("LargeBlockSlideDoor")
        buildAndCheckType<TerminalBlock>("LargeBlockSlideDoor")
        buildAndCheckType<FunctionalBlock>("LargeBlockSlideDoor")
    }


    private inline fun <reified T> SpaceEngineers.buildAndCheckType(blockType: String) {
        val z = 1000
        val definitionId = DefinitionId.cubeBlock(blockType)
        admin.character.teleport(Vec3(0, 0, z + 15), Vec3.FORWARD, Vec3.UP)
        observer.observeNewBlocks()
        admin.blocks.placeAt(definitionId, Vec3(0, 0, z + 0), Vec3.FORWARD, Vec3.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.definitionId.type, blockType)
        assertTrue(block is T)
        admin.blocks.remove(block.id)
    }
}
