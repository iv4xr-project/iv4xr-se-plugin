package spaceEngineers.labrecruits

import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.model.DoorBase
import spaceEngineers.model.TerminalBlock
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockByCustomName
import spaceEngineers.model.typing.DefinitionIds

class LabRecruitsController(
    val spaceEngineers: ExtendedSpaceEngineers,
    val maximumButtonReachDistance: Float = 3.5f,
) {

    private suspend fun <T> se(block: suspend ExtendedSpaceEngineers.() -> T): T {
        return block(spaceEngineers)
    }

    private fun <T> seSync(block: ExtendedSpaceEngineers.() -> T): T {
        return block(spaceEngineers)
    }

    fun observeActiveObjects() = seSync {
        val navGraph = observer.navigationGraph(observer.observeBlocks().grids.first().id)
        val allNodePositions = navGraph.nodes.map { it.data }
        val interestingBlocks = observer.observeBlocks().allBlocks.filterIsInstance<TerminalBlock>().filter { block ->
            allNodePositions.any { block.position.distanceTo(it) < 3f }
        }
        val door = interestingBlocks.filterIsInstance<DoorBase>()
        val buttons = interestingBlocks.filter { it.definitionId == DefinitionIds.ButtonPanel.LargeSciFiButtonTerminal }
        SimpleLabRecruitsObservation(
            door = door,
            buttons = buttons,
            character = observer.observe(),
        )
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

    fun pressButton(buttonId: String) = seSync {
        val button = buttonBlockById(buttonId)
        val distanceToButton = button.position.distanceTo(observer.observe().position)
        if (distanceToButton > maximumButtonReachDistance) {
            error("Too far from the button $buttonId, distance: $distanceToButton, maximum: $maximumButtonReachDistance")
        }
        extensions.blocks.useObject.pressButton(button)
    }
}
