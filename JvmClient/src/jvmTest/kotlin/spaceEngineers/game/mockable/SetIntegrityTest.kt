package spaceEngineers.game.mockable

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockById
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SetIntegrityTest :
    MockOrRealGameTest() {

    @Test
    fun set40percent() = testContext {
        val blockType = "LargeHeavyBlockArmorBlock"
        val z = 1000
        val integrityPercentage = 0.4f
        admin.character.teleport(Vec3F(0, 0, z + 15), Vec3F.FORWARD, Vec3F.UP)
        observer.observeNewBlocks()
        admin.blocks.placeAt(DefinitionId.cubeBlock(blockType), Vec3F(0, 0, z + 0), Vec3F.FORWARD, Vec3F.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.definitionId.type, blockType)
        assertEquals(block.integrity, block.maxIntegrity)
        admin.blocks.setIntegrity(block.id, block.maxIntegrity * integrityPercentage)
        val observedBlock = observer.observeBlocks().blockById(block.id)
        assertEquals(observedBlock.integrity, block.maxIntegrity * integrityPercentage, absoluteTolerance = 0.01f)
    }
}
