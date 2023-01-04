package spaceEngineers.util.generator.map.advanced

import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.graph.Cube3dGraph
import spaceEngineers.model.CubeGrid
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.toFloat
import spaceEngineers.util.generator.map.Orientations

class AdvancedPlacer(
    val map: Map<Vec3I, MutableCell>,
    val start: Vec3I,
    val offset: Vec3F = Vec3F.ZERO,
    val se: ExtendedSpaceEngineers
) {
    fun place(): CubeGrid {
        val grid = placeFirstBlock(start.toFloat() + offset, map.getValue(start))
        val byPriority = map.entries.groupBy { it.value.priority }
        val prioritiesOrdered = byPriority.keys.sorted()
        prioritiesOrdered.forEach {
            val newMap = byPriority[it]!!.associate { it.key to it.value }
            val s = if (newMap.contains(start)) {
                start
            } else {
                newMap.keys.first()
            }
            Cube3dGraph(newMap).dfs(
                start = s
            )
                .filter { (position, _) -> position != start }
                .forEach { (position, cell) ->
                    handleCell(cell, position, grid)
                }
        }
        return grid
    }

    private fun handleCell(
        cell: MutableCell,
        position: Vec3I,
        grid: CubeGrid
    ) {
        cell.orientations.forEach { orientation ->
            try {
                placeCell(cell, position, grid, orientation)
                return
            } catch (e: Exception) {
                // try again
            }
        }
        // TODO: if character is very close to the block, teleport? or let know?
        // TODO: throw the previous exception instead or use it as cause at least
        error("couldn't place $cell at $position")
    }

    private fun placeCell(
        cell: MutableCell,
        position: Vec3I,
        grid: CubeGrid,
        orientation: Orientations
    ) {
        val blockId = se.admin.blocks.placeInGrid(
            blockDefinitionId = cell.id,
            minPosition = position,
            color = cell.color,
            gridId = grid.id,
            orientationForward = orientation.forward,
            orientationUp = orientation.up,
        )
        cell.customName?.let {
            se.admin.blocks.terminalBlock.setCustomName(blockId, it)
        }
        se.admin.blocks.setIntegrity(blockId, 9999999999f)
    }

    private fun placeFirstBlock(position: Vec3F, cell: MutableCell): CubeGrid {
        return se.admin.blocks.placeAt(
            position = position,
            blockDefinitionId = cell.id,
            orientationForward = cell.orientations.first().forward.toFloat(),
            orientationUp = cell.orientations.first().up.toFloat(),
            color = cell.color,
        ).apply {
            se.admin.blocks.setIntegrity(this.blocks.first().id, 999999999f)
        }
    }
}
