package spaceEngineers.controller.extensions

import spaceEngineers.commands.ObservationArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.controller.CharacterController
import spaceEngineers.model.Observation

fun CharacterController.observe(): Observation {
    return observe(ObservationArgs())
}

fun CharacterController.observe(observationMode: ObservationMode): Observation {
    return observe(ObservationArgs(observationMode))
}

