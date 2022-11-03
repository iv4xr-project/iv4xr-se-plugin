package spaceEngineers.labrecruits

import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.model.DoorBase
import spaceEngineers.model.TerminalBlock
import spaceEngineers.model.extensions.blockByCustomName

class LabRecruitsController(
    val spaceEngineers: ExtendedSpaceEngineers,
    val maximumButtonReachDistance: Float = 3.5f,
) {

    suspend fun <T> se(block: suspend ExtendedSpaceEngineers.() -> T): T {
        return block(spaceEngineers)
    }

    fun <T> seSync(block: ExtendedSpaceEngineers.() -> T): T {
        return block(spaceEngineers)
    }


    fun buttonBlockById(buttonId: String) = seSync {
        observer.observeBlocks().blockByCustomName(buttonId)
    }

    fun doorBlockById(doorId: String) = seSync {
        observer.observeBlocks().blockByCustomName(doorId) as DoorBase
    }


    suspend fun goToButton(buttonId: String) = se {
        extensions.character.navigation.navigateToBlock(
            buttonBlockById(buttonId).id
        )
    }

    suspend fun goToDoor(doorId: String) = se {
        extensions.character.navigation.navigateToBlock(
            doorBlockById(doorId).id
        )
    }

    suspend fun pressButton(buttonId: String) = se {
        val button = buttonBlockById(buttonId)
        val distanceToButton = button.position.distanceTo(observer.observe().position)
        if (distanceToButton > maximumButtonReachDistance) {
            error("Too far from the button ${buttonId}, distance: $distanceToButton, maximum: $maximumButtonReachDistance")
        }
        extensions.blocks.useObject.pressButton(button)
    }

}
