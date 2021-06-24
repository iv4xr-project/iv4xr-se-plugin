package spaceEngineers.game

import spaceEngineers.model.Vec3
import spaceEngineers.model.extensions.allBlocks
import testhelp.spaceEngineers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PlaceRemoveTest {

    @Test
    fun placeAndRemove() = spaceEngineers() {
        val blockType = "RealWheel"
        val z = 1000
        //session.loadFromTestResources("simple-place-grind-torch")
        character.teleport(Vec3(0, 0, z + 15), Vec3.FORWARD, Vec3.UP)
        observer.observeNewBlocks()
        items.placeAt(blockType, Vec3(0, 0, z + 0), Vec3.FORWARD, Vec3.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.blockType, blockType)
        assertTrue(observer.observeBlocks().allBlocks.any { it.blockType == blockType })
        items.remove(block.id)
        assertTrue(observer.observeBlocks().allBlocks.none { it.blockType == blockType })
    }
}
