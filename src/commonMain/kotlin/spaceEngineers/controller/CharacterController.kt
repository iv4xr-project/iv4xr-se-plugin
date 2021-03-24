package spaceEngineers.controller

import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.MoveTowardsArgs
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.model.SeObservation


interface CharacterController {

    fun moveAndRotate(movementArgs: MovementArgs): SeObservation

    fun moveTowards(moveTowardsArgs: MoveTowardsArgs): SeObservation

    fun observe(observationArgs: ObservationArgs): SeObservation

    fun interact(interactionArgs: InteractionArgs): SeObservation
}

