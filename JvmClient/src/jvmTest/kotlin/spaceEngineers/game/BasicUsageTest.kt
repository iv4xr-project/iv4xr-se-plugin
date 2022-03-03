package spaceEngineers.game

import org.junit.jupiter.api.Disabled
import spaceEngineers.controller.JvmSpaceEngineersBuilder
import spaceEngineers.controller.SpaceEngineers.Companion.DEFAULT_AGENT_ID
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import testhelp.SIMPLE_PLACE_GRIND_TORCH
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class BasicUsageTest {


    /**
     * This scenario:
     * - Teleports character to a location.
     * - Creates a block there using admin placeAt function and checks the results.
     * - Removes the block again using admin remove and checks, that the block is gone.
     */
    @Disabled("Disabled for building whole project, enable manually by uncommenting.")
    @Test
    fun placeBlock() {
        // We create SpaceEngineers interface, that connects to local Space Engineers game.
        val context = JvmSpaceEngineersBuilder.default().localhost(DEFAULT_AGENT_ID)
        with(context) {
            // We load testing scenario.
            session.loadFromTestResources(SIMPLE_PLACE_GRIND_TORCH)
            // Create ID of the block, that we are gonna build.
            val blockDefinitionId = DefinitionId.cubeBlock("LargeBlockSmallGenerator")
            val z = 1000
            // We teleport character to a specific location and orientation.
            admin.character.teleport(Vec3F(0, 0, z + 15), Vec3F.FORWARD, Vec3F.UP)
            // We observe for new blocks to ensure all the existing blocks won't be "new" anymore.
            observer.observeNewBlocks()
            // We place a block.
            val blockId =
                admin.blocks.placeAt(blockDefinitionId, Vec3F(0, 0, z + 0), Vec3F.FORWARD, Vec3F.UP)
            // We observe for new blocks.
            val block = observer.observeNewBlocks().allBlocks.first()
            // We expect block to be there and to have all the expected properties:
            assertEquals(block.id, blockId)
            assertEquals(block.definitionId.type, block.definitionId.type)
            assertTrue(observer.observeBlocks().allBlocks.any { it.definitionId.type == blockDefinitionId.type })
            assertEquals(12065.0f, block.integrity)
            assertEquals(block.maxIntegrity, block.integrity)
            // We remove the block.
            admin.blocks.remove(block.id)
            // We expect the block to be removed now.
            assertTrue(observer.observeBlocks().allBlocks.none { it.definitionId.type == blockDefinitionId.type })
        }
    }
}
