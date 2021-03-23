package spaceEngineers.controller

import spaceEngineers.commands.*
import spaceEngineers.model.SeObservation
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.allBlocks

class ContextControllerWrapper(
    val controller: CharacterController,
    val context: SpaceEngineersTestContext = SpaceEngineersTestContext()
) : CharacterController {

    fun observeNewBlocks(): SeObservation {
        return observe(ObservationMode.NEW_BLOCKS).apply {
            context.updateNewBlocks(allBlocks)
        }
    }

    fun observeBlocks(): SeObservation {
        return observe(ObservationMode.BLOCKS)
    }

    fun startUsingTool(): SeObservation {
        return interact(InteractionArgs(InteractionType.BEGIN_USE))
    }

    fun endUsingTool(): SeObservation {
        return interact(InteractionArgs(InteractionType.END_USE))
    }


    fun equipAndPlace(toolbarLocation: ToolbarLocation): SeObservation {
        equip(toolbarLocation)
        return controller.interact(InteractionArgs(InteractionType.PLACE))
    }

    fun equip(toolbarLocation: ToolbarLocation): SeObservation {
        return controller.interact(
            InteractionArgs(
                InteractionType.EQUIP,
                slot = toolbarLocation.slot,
                page = toolbarLocation.page
            )
        )
    }

    fun updateObservation(observation: SeObservation) {
        context.update(observation)
    }

    override fun moveAndRotate(movementArgs: MovementArgs): SeObservation {
        return controller.moveAndRotate(movementArgs).apply(::updateObservation)
    }

    override fun moveTowards(moveTowardsArgs: MoveTowardsArgs): SeObservation {
        return controller.moveTowards(moveTowardsArgs).apply(::updateObservation)
    }

    override fun observe(observationArgs: ObservationArgs): SeObservation {
        return controller.observe(observationArgs).apply(::updateObservation)
    }

    override fun interact(interactionArgs: InteractionArgs): SeObservation {
        return controller.interact(interactionArgs).apply(::updateObservation)
    }
}