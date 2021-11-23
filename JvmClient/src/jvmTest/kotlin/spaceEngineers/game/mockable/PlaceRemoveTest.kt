package spaceEngineers.game.mockable

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
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
        admin.character.teleport(Vec3F(0, 0, z + 15), Vec3F.FORWARD, Vec3F.UP)
        observer.observeNewBlocks()
        val blockId = admin.blocks.placeAt(DefinitionId.create("Wheel", blockType), Vec3F(0, 0, z + 0), Vec3F.FORWARD, Vec3F.UP)
        val block = observer.observeNewBlocks().allBlocks.first()
        assertEquals(block.id, blockId)
        assertEquals(block.definitionId.type, blockType)
        assertTrue(observer.observeBlocks().allBlocks.any { it.definitionId.type == blockType })
        admin.blocks.remove(block.id)
        assertTrue(observer.observeBlocks().allBlocks.none { it.definitionId.type == blockType })
    }
}
