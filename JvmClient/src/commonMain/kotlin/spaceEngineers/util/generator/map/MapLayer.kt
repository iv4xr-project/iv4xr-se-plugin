package spaceEngineers.util.generator.map

import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap

interface MapLayer {
    val width: Int
    val height: Int
    operator fun get(x: Int, y: Int): BlockPlacementInformation?
}




fun simplifiedMap(mapLayer: LabRecruitsMap): LabRecruitsMap = with(mapLayer) {
    return LabRecruitsMap(
        cells = cells.simplifiedArray().removeUselessFloors(),
        mappings = mappings,
        doors = doors,
        buttons = buttons
    )
}

fun LabRecruitsMap.toSimplified(): LabRecruitsMap {
    return simplifiedMap(this)
}
