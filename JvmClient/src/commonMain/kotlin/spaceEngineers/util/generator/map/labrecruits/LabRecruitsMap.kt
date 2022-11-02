package spaceEngineers.util.generator.map.labrecruits


import spaceEngineers.controller.extensions.toNullIfMinusOne
import spaceEngineers.util.generator.map.BlockPlacementInformation
import spaceEngineers.util.generator.map.MapLayer
import spaceEngineers.util.generator.maze.Direction

class LabRecruitsMap(
    val cells: Array<Array<LabRecruitCell?>>,
    override val width: Int = cells.size,
    override val height: Int = cells.first().size,
    val mappings: Map<Button, Set<Door>>,
    val doors: Map<DoorId, Door>,
    val buttons: Map<ButtonId, Button>,
) : MapLayer {
    override fun get(x: Int, y: Int): BlockPlacementInformation? {
        return cells[x][y]
    }

    fun placeGenerator() {
        placeCustomBlock(Generator)
    }

    fun placeGravityGenerator() {
        placeCustomBlock(GravityGenerator)
    }

    fun placeCustomBlock(cell: LabRecruitCell, replacedCell: LabRecruitCell = Wall) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (cells[x][y] == replacedCell) {
                    cells[x][y] = cell
                    return
                }
            }
        }

    }

    companion object {
        fun fromLines(lines: List<String>): LabRecruitsMap {
            val mapStartIndex = lines.indexOfFirst { it.startsWith("|") }
            val buttonMapping = lines.subList(0, mapStartIndex)
            val mapMapping = lines.subList(mapStartIndex + 1, lines.size)
            val secondLevelStartIndex = mapMapping.indexOfFirst { it.startsWith("|") }.toNullIfMinusOne() ?: lines.size
            val firstLevel = lines.subList(mapStartIndex, secondLevelStartIndex)
            val map = parseMap(firstLevel)
            val flatMap = map.flatten()
            val doors = flatMap.filterIsInstance<Door>().associateBy { it.id }
            val buttons = flatMap.filterIsInstance<Button>().associateBy { it.id }
            val mappings = parseMapping(buttonMapping, door = doors, buttons = buttons)
            return LabRecruitsMap(cells = map, mappings = mappings, doors = doors, buttons = buttons)
        }

        private fun parseMapping(
            lines: List<String>,
            door: Map<DoorId, Door>,
            buttons: Map<ButtonId, Button>
        ): Map<Button, Set<Door>> {
            return lines.associate {
                val cells = it.split(",")
                (buttons.get(cells.first()) ?: Button(cells.first())) to cells.subList(1, cells.size)
                    .map { doorId -> door.get(doorId) ?: Door(doorId, Direction.LEFT) }.toSet()
            }
        }

        fun parseMap(lines: List<String>): Array<Array<LabRecruitCell?>> {
            val width = lines.first().split(",").size
            val height = lines.size
            return Array(width) { x ->
                Array(height) { y ->
                    val line = lines[y].split(",")
                    cellFromText(line[x])
                }
            }
        }

    }
}
