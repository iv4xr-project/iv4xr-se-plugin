package spaceEngineers.game.mockable

import spaceEngineers.model.Vec3
import spaceEngineers.model.extensions.allBlocks
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PlaceRemoveTest : MockOrRealGameTest() {

    @Test
    fun placeAndRemove() = testContext {
        val blockType = "RealWheel"
        val z = 1000
        admin.character.teleport(Vec3(0, 0, z + 15), Vec3.FORWARD, Vec3.UP)
        observer.observeNewBlocks()
        admin.blocks.placeAt(blockType, Vec3(0, 0, z + 0), Vec3.FORWARD, Vec3.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.blockType, blockType)
        assertTrue(observer.observeBlocks().allBlocks.any { it.blockType == blockType })
        admin.blocks.remove(block.id)
        assertTrue(observer.observeBlocks().allBlocks.none { it.blockType == blockType })
    }
}
