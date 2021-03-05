package spaceEngineers.controller

import spaceEngineers.model.SeObservation
import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.MoveTowardsArgs
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.ObservationArgs


interface Vector3 : Vector2 {
    val z: Float
}

interface Vector2 {
    val x: Float
    val y: Float
}


fun CharacterController.observe(): SeObservation {
    return observe(ObservationArgs())
}


interface CharacterController {

    fun moveAndRotate(movementArgs: MovementArgs): SeObservation

    fun moveTowards(moveTowardsArgs: MoveTowardsArgs): SeObservation

    fun observe(observationArgs: ObservationArgs): SeObservation

    fun interact(interactionArgs: InteractionArgs): SeObservation
}

