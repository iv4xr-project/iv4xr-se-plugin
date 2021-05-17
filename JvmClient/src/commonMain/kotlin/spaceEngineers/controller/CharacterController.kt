package spaceEngineers.controller

import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.MoveTowardsArgs
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.model.Observation


interface CharacterController {

    fun moveAndRotate(movementArgs: MovementArgs): Observation

    fun moveTowards(moveTowardsArgs: MoveTowardsArgs): Observation

    fun observe(observationArgs: ObservationArgs): Observation

    fun interact(interactionArgs: InteractionArgs): Observation
}

