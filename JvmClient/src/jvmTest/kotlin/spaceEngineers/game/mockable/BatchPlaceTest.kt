package spaceEngineers.game.mockable

import spaceEngineers.model.BlockLocation
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.typing.DefinitionIds
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BatchPlaceTest : MockOrRealGameTest() {

    @Test
    fun placeInGrid() = testContext {
        val start = Vec3F.ZERO + Vec3F.FORWARD * 1000f
        admin.character.teleport(start + Vec3F.FORWARD * -10f, Vec3F.FORWARD, Vec3F.UP)

        val definitionId = DefinitionIds.CubeBlock.LargeBlockArmorBlock
        val grid = admin.blocks.placeAt(definitionId, start)
        val positions = List(10) {
            Vec3I(it + 1, 0, 0)
        }
        positions.forEach {
            admin.blocks.placeInGrid(
                definitionId,
                minPosition = it,
                gridId = grid.id,
            )
        }
        assertEquals(11, observer.observeBlocks().grids.first { it.id == grid.id }.blocks.size)
    }

    @Test
    fun placeInGridBatch() = testContext {
        val start = Vec3F.ZERO + Vec3F.FORWARD * 1000f
        admin.character.teleport(start + Vec3F.FORWARD * -10f, Vec3F.FORWARD, Vec3F.UP)

        val definitionId = DefinitionIds.CubeBlock.LargeBlockArmorBlock
        val grid = admin.blocks.placeAt(definitionId, start)
        val positions = List(10) {
            Vec3I(it + 1, 0, 0)
        }
        admin.blocks.batchPlaceInGrid(
            gridId = grid.id,
            blockPlacementConfigs = positions.map {
                BlockLocation(minPosition = it, definitionId = definitionId)
            }
        )
        assertEquals(11, observer.observeBlocks().grids.first { it.id == grid.id }.blocks.size)
    }
}
