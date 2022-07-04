package spaceEngineers.util.generator.map

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.LARGE_BLOCK_CUBE_SIDE_SIZE
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I

class MapPlacer(
    val map: MapLayer,
    val spaceEngineers: SpaceEngineers,
    val floorDefinitionId: DefinitionId = DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock"),
) {

    private fun generateFloor(initialGridId: String? = null, z: Int = 0): String {
        val definitionId = floorDefinitionId
        var gridId: String? = initialGridId
        for (x in 0 until map.width) {
            for (y in 0 until map.height) {
                print(".")
                if (gridId == null) {
                    placeAt(
                        blockDefinitionId = definitionId,
                        position = Vec3F(x, y, z) * LARGE_BLOCK_CUBE_SIDE_SIZE,
                        orientationForward = Vec3F.FORWARD,
                        orientationUp = Vec3F.UP,
                    )
                    gridId = spaceEngineers.observer.observeBlocks().grids.first().id
                } else {
                    placeInGrid(
                        gridId = gridId,
                        blockDefinitionId = definitionId,
                        orientationUp = Vec3I.UP,
                        orientationForward = Vec3I.FORWARD,
                        minPosition = Vec3I(x, y, z)
                    )
                }
            }
            println(" $x")
        }
        return gridId ?: error("No grid id?!")
    }

    private fun generateLevel(gridId: String, z: Int = 1) {
        for (x in 0 until map.width) {
            for (y in 0 until map.height) {
                map[x, y]?.let {
                    cell ->
                    print(cell.javaClass.simpleName.first())
                    placeInGrid(
                        gridId = gridId,
                        blockDefinitionId = cell.blockId,
                        orientationUp = cell.orientationUp,
                        orientationForward = cell.orientationForward,
                        minPosition = Vec3I(x, y, z)
                    )
                } ?: print(" ")

            }
            println(" $x")
        }
    }

    fun generate() {
        spaceEngineers.admin.character.teleport(
            Vec3F(10, 10, 10)
        )
        generateFloor().let {
            generateLevel(it)
        }

    }


    private fun placeAt(
        blockDefinitionId: DefinitionId,
        position: Vec3F,
        orientationForward: Vec3F,
        orientationUp: Vec3F
    ) {
        //return
        spaceEngineers.admin.blocks.placeAt(blockDefinitionId, position, orientationForward, orientationUp)
    }

    private fun placeInGrid(
        gridId: String,
        blockDefinitionId: DefinitionId,
        orientationUp: Vec3I,
        orientationForward: Vec3I,
        minPosition: Vec3I
    ) {
        //return
        spaceEngineers.admin.blocks.placeInGrid(
            blockDefinitionId = blockDefinitionId,
            gridId = gridId,
            orientationUp = orientationUp,
            orientationForward = orientationForward,
            minPosition = minPosition,
        )
    }
}
