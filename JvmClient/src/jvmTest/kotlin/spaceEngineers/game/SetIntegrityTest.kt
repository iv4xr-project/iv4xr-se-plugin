package spaceEngineers.game

import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.Vec3
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockById
import testhelp.assertFloatEquals
import testhelp.spaceEngineers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SetIntegrityTest {

    @Test
    fun set40percent() = spaceEngineers() {
        val blockType = "LargeHeavyBlockArmorBlock"
        val z = 1000
        val integrityPercentage = 0.4f
        session.loadFromTestResources("simple-place-grind-torch")
        character.teleport(Vec3(0, 0, z + 15), Vec3.FORWARD, Vec3.UP)
        observer.observeNewBlocks()
        items.placeAt(blockType, Vec3(0, 0, z + 0), Vec3.FORWARD, Vec3.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.blockType, blockType)
        assertEquals(block.integrity, block.maxIntegrity)
        items.setIntegrity(block.id, block.maxIntegrity * integrityPercentage)
        val observedBlock = observer.observeBlocks().blockById(block.id)
        assertFloatEquals(observedBlock.integrity, block.maxIntegrity * integrityPercentage, diff = 0.01f)
    }
}
