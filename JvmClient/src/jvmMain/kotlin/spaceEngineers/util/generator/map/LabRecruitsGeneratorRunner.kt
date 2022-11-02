package spaceEngineers.util.generator.map

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.controller.extensions.removeAllBlocks
import spaceEngineers.model.TerminalBlock
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.util.generator.map.labrecruits.Button
import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import java.io.File


fun main() {
    val lines = File("./src/jvmMain/resources/LabRecruits_level_small.csv").readLines()

    val labRecruitsMap = LabRecruitsMap.fromLines(lines)
    labRecruitsMap.placeGenerator()
    labRecruitsMap.placeGravityGenerator()
    val spaceEngineers = SpaceEngineersJavaProxyBuilder().localhost()
    spaceEngineers.removeAllBlocks()
    val labRecruitsMapPlacer = MapPlacer(labRecruitsMap, spaceEngineers = spaceEngineers)
    val gridId = labRecruitsMapPlacer.generate()
    spaceEngineers.createGroups(gridId, labRecruitsMap)
    spaceEngineers.mapButtons(gridId, labRecruitsMap)
}

fun SpaceEngineers.createGroups(gridId: String, map: LabRecruitsMap) {
    val blockIdsByCustomName = observer.observeBlocks().allBlocks.filterIsInstance<TerminalBlock>().associate {
        it.customName to it.id
    }
    map.mappings.forEach { (button, doors) ->
        admin.blocks.createOrUpdateGroup(
            "group-${button.id}",
            gridId,
            doors.map { blockIdsByCustomName.getValue(it.id) })
    }
}

fun SpaceEngineers.mapButtons(gridId: String, map: LabRecruitsMap) {
    val blockIdsByCustomName = observer.observeBlocks().allBlocks.filterIsInstance<TerminalBlock>().associate {
        it.customName to it.id
    }

    map.buttons.values.forEach { button ->
        admin.blocks.mapButtonToGroup(
            buttonBlockId =blockIdsByCustomName.getValue(button.id),
            buttonIndex = 0,
            action = "Open",
            groupName = "group-${button.id}"
        )
    }
}
