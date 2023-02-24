package spaceEngineers.util.generator.map

import spaceEngineers.util.generator.map.labrecruits.Floor
import spaceEngineers.util.generator.map.labrecruits.LabRecruitCell
import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import spaceEngineers.util.generator.map.labrecruits.UnnecessaryFloor

interface MapLayer {
    val width: Int
    val height: Int
    operator fun get(x: Int, y: Int): BlockPlacementInformation?
}

fun simplifiedMap(mapLayer: LabRecruitsMap): LabRecruitsMap = with(mapLayer) {
    return LabRecruitsMap(
        cells = cells.simplifiedArray().removeUselessFloors().connectXToBase(),
        mappings = mappings,
        doors = doors,
        buttons = buttons
    )
}

private fun Array<Array<LabRecruitCell?>>.connectXToBase(y: Int = 0, nextY: Int = 1): Array<Array<LabRecruitCell?>> {
    for (x in 0 until width) {
        if (this[x][y] != UnnecessaryFloor) {
            break
        }
        this[x][y] = Floor
        if (this[x][y + nextY] != UnnecessaryFloor) {
            break
        }
    }
    return this
}

fun LabRecruitsMap.toSimplified(): LabRecruitsMap {
    return simplifiedMap(this)
}
