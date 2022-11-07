package spaceEngineers.util.generator.map

import spaceEngineers.controller.proxy.BatchCallable
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.proxy.executeIfNotNull
import spaceEngineers.model.*
import spaceEngineers.model.extensions.blockById
import spaceEngineers.model.extensions.toFloat
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.model.typing.DefinitionIds.CubeBlock.LargeHeavyBlockArmorBlock
import spaceEngineers.util.generator.map.labrecruits.*

class MapPlacer(
    val map: MapLayer,
    val spaceEngineers: SpaceEngineers,
    val floorPlacer: DataBlockPlacementInformation = DataBlockPlacementInformation(
        blockId = LargeHeavyBlockArmorBlock,
        orientations = listOf(Orientations()),
        color = null,
    ),
    val batchCallable: BatchCallable? = null
) {

    val blockIdsToCustomNames = mutableMapOf<String, String>()

    private fun placeFirstBlock(position: Vec3F): String {
        placeAt(
            position = position,
            blockDefinitionId = floorPlacer.blockId,
            orientationForward = floorPlacer.orientationForward.toFloat(),
            orientationUp = floorPlacer.orientationUp.toFloat(),
            color = floorPlacer.color,
        )
        return spaceEngineers.observer.observeBlocks().grids.first().id
    }

    private fun generateFloor(gridId: String, level: Int = 0) = batch {
        for (z in 0 until map.height) {
            for (x in 0 until map.width) {
                print(".")
                if (z == 0 && x == 0) {
                    continue
                }
                val position = Vec3I(x, level, z)
                map[x, z]?.let { cell ->
                    if (cell is Floor || cell is UnnecessaryFloor) {
                        processCell(cell, gridId, position)
                    } else {
                        processCell(floorPlacer.copy(color = cell.color ?: floorPlacer.color), gridId, position)
                    }
                }
            }
            println(" $z")
        }
    }

    private fun generateFunctionalBlocks(gridId: String) =
        filterCells(1) {
            it != Wall && it::class !in setOf(
                Agent::class,
                Floor::class,
                UnnecessaryFloor::class
            )
        }.forEach { (cell, position) ->
            processCell(cell, gridId, position)
        }

    data class PlacementInformation(val blockPlacementInformation: BlockPlacementInformation, val position: Vec3I)

    private fun filterCells(
        level: Int,
        cellFilter: (BlockPlacementInformation) -> Boolean,
    ): Sequence<PlacementInformation> = sequence {
        for (z in 0 until map.height) {
            for (x in 0 until map.width) {
                map[x, z]?.also { cell ->
                    if (cellFilter(cell)) {
                        val position = Vec3I(x, level, z)
                        yield(PlacementInformation(cell, position))
                    }
                }
            }
        }
    }

    private fun generateSpawn(level: Int = 1) =
        filterCells(level) { it::class == Agent::class }.forEach { (cell, position) ->
            val positionInGrid = position + Vec3I(-1, level, 0)
            val blockId = placeAt(
                cell.blockId,
                position = positionInGrid * LARGE_BLOCK_CUBE_SIDE_SIZE,
                color = cell.color,
                orientationForward = cell.orientationForward.toFloat(),
                orientationUp = cell.orientationUp.toFloat()
            )
            val gridId = spaceEngineers.observer.observeBlocks().grids.first { it.blocks.any { it.id == blockId } }.id
            placeInGrid(
                gridId = gridId,
                blockDefinitionId = DefinitionIds.Reactor.LargeBlockSmallGenerator,
                minPosition = Vec3I(0, 1, 1),
                orientationUp = Vec3I.UP,
                orientationForward = Vec3I.FORWARD,
                color = null,
            )
        }


    private fun generateWalls(gridId: String) =
        filterCells(1) { it == Wall }.forEach { (cell, position) ->
            processCell(cell, gridId, position)
        }

    private fun <T> List<T>.runUntilSuccess(block: (T) -> Unit) {
        check(isNotEmpty()) { "List is empty!" }
        if (size == 1) {
            block(first())
            return
        }
        forEachIndexed { index, it ->
            try {
                block(it)
                return
            } catch (e: Exception) {
                // ok
                if (index == lastIndex) {
                    throw e
                }
            }
        }
    }

    private fun processCell(cell: BlockPlacementInformation, gridId: String, position: Vec3I) {
        cell.orientations.runUntilSuccess {
            val blockId = placeInGrid(
                gridId = gridId,
                blockDefinitionId = cell.blockId,
                orientationUp = it.up,
                orientationForward = it.forward,
                minPosition = position + cell.offset,
                color = cell.color,
            )
            cell.customName?.let { customName ->
                spaceEngineers.observer.observeBlocks().blockById(blockId)
                blockIdsToCustomNames[customName] = blockId
            }
        }
    }

    private fun setupCustomNames() = batch {
        blockIdsToCustomNames.map { (customName, blockId) ->
            spaceEngineers.admin.blocks.setCustomName(blockId, customName)
        }
    }

    private fun <T : Any?> batch(block: () -> T): T {
        return batchCallable.executeIfNotNull(block)
    }

    fun generate(
        position: Vec3F = Vec3F.ZERO,
        teleportPosition: Vec3F = position + Vec3F(10, 10, 10)
    ): String {
        blockIdsToCustomNames.clear()
        spaceEngineers.admin.character.teleport(teleportPosition)
        val gridId = placeFirstBlock(position = position)
        generateFloor(gridId = gridId)
        generateWalls(gridId)
        generateFunctionalBlocks(gridId)
        generateSpawn()
        setupCustomNames()
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
            blockDefinitionId, position, orientationForward, orientationUp, color
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
