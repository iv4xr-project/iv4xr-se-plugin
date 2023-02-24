package spaceEngineers.controller.useobject

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.Block
import spaceEngineers.model.DoorBase

class UseObjectExtensions(val spaceEngineers: SpaceEngineers) {

    fun toggle(door: DoorBase) = se {
        usePrimaryAction(door, name = "MyUseObjectAirtightDoors")
    }

    fun closeIfNotClosed(door: DoorBase) = se {
        if (door.open) {
            toggle(door)
        }
    }

    fun openIfNotOpened(door: DoorBase) = se {
        if (!door.open) {
            toggle(door)
        }
    }

    fun pressButton(button: Block, index: Int = 0) = se {
        usePrimaryAction(button, name = "MyUseObjectPanelButton", index = index)
    }

    fun usePrimaryAction(block: Block, name: String, index: Int = 0) = se {
        val useObject = block.useObjects.filter { it.name == name }.getOrNull(index)
            ?: error("No '$name' use object found for block ${block.id}, index $index")
        val wholeIndex = block.useObjects.indexOf(useObject)
        admin.character.use(block.id, wholeIndex, useObject.primaryAction)
    }

    fun useSecondaryAction(block: Block, name: String, index: Int = 0) = se {
        val useObject = block.useObjects.filter { it.name == name }.getOrNull(index)
            ?: error("No '$name' use object found for block ${block.id}, index $index")
        val wholeIndex = block.useObjects.indexOf(useObject)
        admin.character.use(block.id, wholeIndex, useObject.secondaryAction)
    }

    private fun <T> se(block: SpaceEngineers.() -> T): T {
        return block(spaceEngineers)
    }
}
