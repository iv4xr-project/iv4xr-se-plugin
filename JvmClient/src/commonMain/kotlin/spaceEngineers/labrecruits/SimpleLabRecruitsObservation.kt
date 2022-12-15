package spaceEngineers.labrecruits

import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.DoorBase
import spaceEngineers.model.TerminalBlock

data class SimpleLabRecruitsObservation(
    val door: List<DoorBase>,
    val buttons: List<TerminalBlock>,
    val character: CharacterObservation,
) {
    override fun toString(): String {
        return """
            ${door.map { it.customName }}
            ${buttons.map { it.customName }}
            ${character.position}
        """.trimIndent()
    }
}
