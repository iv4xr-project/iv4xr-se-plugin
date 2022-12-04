package spaceEngineers.util.generator.map.labrecruits

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

    fun doorsByButtonId(buttonId: ButtonId): Set<Door> {
        return mappings.getValue(buttons.getValue(buttonId))
    }

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

    fun toCsv(): String {
        return (generateHeadersCsv() + "\n" + generateMapCsv()).trim()
    }

    private fun generateHeadersCsv() = mappings.keys.sortedWith(labRecruitsButtonComparator).map {
        it.id to (mappings[it]?.map { it.id }?.sortedWith(labRecruitsIdComparator) ?: emptyList())
    }.joinToString("\n") { (buttonId, door) ->
        "$buttonId,${door.joinToString(",")}"
    }

    private fun generateMapCsv(): String {
        return "|" + (0 until height).map { y ->
            (0 until width).map { x -> cells[x][y] }.joinToString(",") { it?.toCsv() ?: "" }
        }.joinToString("\n")
    }

    companion object {
        fun fromString(text: String): LabRecruitsMap {
            val parts = text.split("|")
            val mapping = parts.first().trim().lines().filter { it.isNotBlank() }
            val firstFloor = parts[1].trim().lines().filter { it.isNotBlank() }
            val map = parseMap(firstFloor)
            val flatMap = map.flatten()
            val doors = flatMap.filterIsInstance<Door>().associateBy { it.id }
            val buttons = flatMap.filterIsInstance<Button>().associateBy { it.id }
            val mappings = parseMapping(mapping, door = doors, buttons = buttons)
            return LabRecruitsMap(cells = map, mappings = mappings, doors = doors, buttons = buttons)
        }

        private fun parseMapping(
            lines: List<String>,
            door: Map<DoorId, Door>,
            buttons: Map<ButtonId, Button>
        ): Map<Button, Set<Door>> {
            if (lines.isEmpty()) {
                return emptyMap()
            }
            return lines.associate { it ->
                val cells = it.split(",").filter { it.isNotBlank() }

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
