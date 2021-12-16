package spaceEngineers.game.mockable

import spaceEngineers.model.*
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockById
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PlaceRemoveTest : MockOrRealGameTest() {

    @Test
    fun placeInGrid() = testContext {
        observer.observeNewBlocks()
        val observation = observer.observeBlocks()
        val blockType1 = "LargeHeavyBlockArmorBlock"
        val blockType2 = "LargeBlockArmorBlock"
        val blockDefinition1 = DefinitionId.cubeBlock(blockType1)
        val blockDefinition2 = DefinitionId.cubeBlock(blockType2)
        observation.allBlocks.forEach {
            admin.blocks.remove(it.id)
        }

        val blockId1 = admin.blocks.placeAt(
            blockDefinition1,
            observation.character.position + Vec3F(5f, 0f, 0f),
            Vec3F.FORWARD,
            Vec3F.UP
        )
        val grid = observer.observeBlocks().grids.first()
        val blockId2 = admin.blocks.placeInGrid(
            blockDefinition2, grid.id, Vec3I.ZERO + Vec3I(1, 0, 0), Vec3I.FORWARD, Vec3I.UP
        )
        val blocks = observer.observeBlocks()
        val block1 = blocks.blockById(blockId1)
        val block2 = blocks.blockById(blockId2)
        assertEquals(Vec3I.ZERO, block1.gridPosition)
        assertEquals(Vec3I.ZERO + Vec3I(1, 0, 0), block2.gridPosition)
        assertEquals(2, blocks.allBlocks.size)
        blocks.allBlocks.forEach { block ->
            checkBlockOwnership(block, observation.character)
        }
    }

    @Test
    fun placeAndRemove() = testContext {
        val blockType = "LargeBlockSmallGenerator"
        val z = 1000
        admin.character.teleport(Vec3F(0, 0, z + 15), Vec3F.FORWARD, Vec3F.UP)
        observer.observeNewBlocks()
        val blockId =
            admin.blocks.placeAt(DefinitionId.cubeBlock(blockType), Vec3F(0, 0, z + 0), Vec3F.FORWARD, Vec3F.UP)
        val observation = observer.observe()
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.id, blockId)
        assertEquals(block.definitionId.type, blockType)
        checkBlockOwnership(block, observation)
        assertTrue(observer.observeBlocks().allBlocks.any { it.definitionId.type == blockType })
        assertEquals(12065.0f, block.integrity)
        assertEquals(block.maxIntegrity, block.integrity)
        admin.blocks.remove(block.id)
        assertTrue(observer.observeBlocks().allBlocks.none { it.definitionId.type == blockType })
    }

    @Test
    fun placeAtSurvivalFunctional() = testContext(scenarioId = "violent-survival") {
        val blockType = "LargeBlockSmallGenerator"
        val blockDefinition = DefinitionId.cubeBlock(blockType)
        val z = 1000
        admin.character.teleport(Vec3F(0, 0, z + 15), Vec3F.FORWARD, Vec3F.UP)
        val obs = observer.observeNewBlocks()
        val blockId =
            admin.blocks.placeAt(blockDefinition, Vec3F(0, 0, z + 0), Vec3F.FORWARD, Vec3F.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.id, blockId)
        assertEquals(block.definitionId.type, blockType)
        checkBlockOwnership(block, obs.character)
        assertTrue(observer.observeBlocks().allBlocks.any { it.definitionId.type == blockType })
        assertEquals(12065.0f, block.integrity)
        assertEquals(block.maxIntegrity, block.integrity)
        admin.blocks.remove(block.id)
        assertTrue(observer.observeBlocks().allBlocks.none { it.definitionId.type == blockType })
    }

    @Test
    fun placeAtSurvival() = testContext(scenarioId = "violent-survival") {
        val blockType = "LargeHeavyBlockArmorBlock"
        val blockDefinition = DefinitionId.cubeBlock(blockType)
        val z = 1000
        admin.character.teleport(Vec3F(0, 0, z + 15), Vec3F.FORWARD, Vec3F.UP)
        val obs = observer.observeNewBlocks()
        val blockId =
            admin.blocks.placeAt(blockDefinition, Vec3F(0, 0, z + 0), Vec3F.FORWARD, Vec3F.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.id, blockId)
        assertEquals(block.definitionId.type, blockType)
        checkBlockOwnership(block, obs.character)
        assertTrue(observer.observeBlocks().allBlocks.any { it.definitionId.type == blockType })
        assertEquals(16500.0f, block.integrity)
        assertEquals(block.maxIntegrity, block.integrity)
        admin.blocks.remove(block.id)
        assertTrue(observer.observeBlocks().allBlocks.none { it.definitionId.type == blockType })
    }

    private fun checkBlockOwnership(block: Block, character: CharacterObservation) {
        if (block.functional) {
            assertEquals(block.ownerId, character.id)
        } else {
            // Non-functional block has 0 ownerId.
            assertEquals(block.ownerId, "0")
        }
    }
}
