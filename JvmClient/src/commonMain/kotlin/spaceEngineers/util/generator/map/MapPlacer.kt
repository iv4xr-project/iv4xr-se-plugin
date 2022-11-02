package spaceEngineers.util.generator.map

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.LARGE_BLOCK_CUBE_SIDE_SIZE
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.toFloat

class MapPlacer(
    val map: MapLayer,
    val spaceEngineers: SpaceEngineers,
    val floorPlacer: BlockPlacementInformation = DataBlockPlacementInformation(
        blockId = DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock"),
        orientationForward = Vec3I.FORWARD,
        orientationUp = Vec3I.UP,
        color = null,
    )
) {

    private fun generateFloor(offset: Vec3F, level: Int = 0): String {
        var gridId: String? = null
        for (x in 0 until map.width) {
            for (z in 0 until map.height) {
                print(".")
                val position = Vec3I(x, level, z)
                if (gridId == null) {
                    placeAt(
                        position = position * LARGE_BLOCK_CUBE_SIDE_SIZE + offset,
                        blockDefinitionId = floorPlacer.blockId,
                        orientationForward = floorPlacer.orientationForward.toFloat(),
                        orientationUp = floorPlacer.orientationUp.toFloat(),
                        color = floorPlacer.color,
                    )
                    gridId = spaceEngineers.observer.observeBlocks().grids.first().id
                } else {
                    processCell(floorPlacer, gridId, position)
                }
            }
            println(" $x")
        }
        return gridId ?: error("No grid id?!")
    }

    private fun generateLevel(gridId: String, level: Int = 1) {
        for (x in 0 until map.width) {
            for (z in 0 until map.height) {
                map[x, z]?.also { cell ->
                    print(cell.javaClass.simpleName.first())
                    processCell(cell, gridId, Vec3I(x, level, z))
                } ?: print(" ")
            }
            println(" $x")
        }
    }

    fun processCell(cell: BlockPlacementInformation, gridId: String, position: Vec3I) {
        val blockId = placeInGrid(
            gridId = gridId,
            blockDefinitionId = cell.blockId,
            orientationUp = cell.orientationUp,
            orientationForward = cell.orientationForward,
            minPosition = position + cell.offset,
            color = cell.color,
        )
        cell.customName?.let { customName ->
            spaceEngineers.admin.blocks.setCustomName(blockId, customName)
        }

    }

    fun generate(offset: Vec3F = Vec3F.ZERO, teleportPosition: Vec3F = offset + Vec3F(10, 10, 10)): String {
        spaceEngineers.admin.character.teleport(teleportPosition)
        val gridId = generateFloor(offset = offset)
        generateLevel(gridId)
        return gridId
    }


    private fun placeAt(
        blockDefinitionId: DefinitionId,
        position: Vec3F,
        orientationForward: Vec3F,
        orientationUp: Vec3F,
        color: Vec3F?,
    ): String {
        return spaceEngineers.admin.blocks.placeAt(
            blockDefinitionId,
            position,
            orientationForward,
            orientationUp,
            color
        )
    }

    private fun placeInGrid(
        gridId: String,
        blockDefinitionId: DefinitionId,
        orientationUp: Vec3I,
        orientationForward: Vec3I,
        minPosition: Vec3I,
        color: Vec3F?,
    ): String {
        return spaceEngineers.admin.blocks.placeInGrid(
            blockDefinitionId = blockDefinitionId,
            gridId = gridId,
            orientationUp = orientationUp,
            orientationForward = orientationForward,
            minPosition = minPosition,
            color = color,
        )
    }
}
