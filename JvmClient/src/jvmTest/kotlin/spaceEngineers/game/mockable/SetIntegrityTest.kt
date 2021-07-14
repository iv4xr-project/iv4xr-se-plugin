package spaceEngineers.game.mockable

import spaceEngineers.model.Vec3
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockById
import testhelp.MockOrRealGameTest
import testhelp.assertFloatEquals
import kotlin.test.Test
import kotlin.test.assertEquals


class SetIntegrityTest :
    MockOrRealGameTest() {

    @Test
    fun set40percent() = testContext {
        val blockType = "LargeHeavyBlockArmorBlock"
        val z = 1000
        val integrityPercentage = 0.4f
        admin.character.teleport(Vec3(0, 0, z + 15), Vec3.FORWARD, Vec3.UP)
        observer.observeNewBlocks()
        admin.blocks.placeAt(blockType, Vec3(0, 0, z + 0), Vec3.FORWARD, Vec3.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.blockType, blockType)
        assertEquals(block.integrity, block.maxIntegrity)
        admin.blocks.setIntegrity(block.id, block.maxIntegrity * integrityPercentage)
        val observedBlock = observer.observeBlocks().blockById(block.id)
        assertFloatEquals(observedBlock.integrity, block.maxIntegrity * integrityPercentage, diff = 0.01f)
    }
}
