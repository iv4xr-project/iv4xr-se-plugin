package spaceEngineers.util.generator.map.labrecruits

import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.controller.extensions.removeAllBlocks
import spaceEngineers.model.DoorBase
import spaceEngineers.model.TerminalBlock
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockByCustomName
import spaceEngineers.util.generator.map.MapPlacer
import java.lang.Thread.sleep

class LabRecruitsMapBuilder(val map: LabRecruitsMap, val spaceEngineers: ExtendedSpaceEngineers) {


    fun generate() {
        map.placeGenerator()
        map.placeGravityGenerator()
        spaceEngineers.removeAllBlocks()
        val labRecruitsMapPlacer = MapPlacer(map, spaceEngineers = spaceEngineers)
        val gridId = labRecruitsMapPlacer.generate()
        createGroups(gridId, map)
        mapButtons(map)
        sleep(50)
        closeAllDoors(map)
    }


    private fun closeAllDoors(map: LabRecruitsMap) = se {
        map.doors.values.forEach { door ->
            val doorBlock = observer.observeBlocks().blockByCustomName(door.id) as DoorBase
            extensions.blocks.useObject.closeIfNotClosed(doorBlock)
        }
    }

    private fun createGroups(gridId: String, map: LabRecruitsMap) = se {
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

    private fun mapButtons(map: LabRecruitsMap) = se {
        val blockIdsByCustomName = observer.observeBlocks().allBlocks.filterIsInstance<TerminalBlock>().associate {
            it.customName to it.id
        }

        map.buttons.values.forEach { button ->
            admin.blocks.mapButtonToGroup(
                buttonBlockId = blockIdsByCustomName.getValue(button.id),
                buttonIndex = 0,
                action = "Open",
                groupName = "group-${button.id}"
            )
        }
    }

    private fun <T> se(block: ExtendedSpaceEngineers.() -> T): T {
        return block(spaceEngineers)
    }

}
