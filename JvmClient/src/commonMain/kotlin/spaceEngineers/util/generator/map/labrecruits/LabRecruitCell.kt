package spaceEngineers.util.generator.map.labrecruits

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.model.typing.DefinitionIds.AirtightSlideDoor.LargeBlockSlideDoor
import spaceEngineers.model.typing.DefinitionIds.ButtonPanel.LargeSciFiButtonTerminal
import spaceEngineers.model.typing.DefinitionIds.CubeBlock.LargeHeavyBlockArmorBlock
import spaceEngineers.model.typing.DefinitionIds.MedicalRoom.LargeMedicalRoom
import spaceEngineers.model.typing.DefinitionIds.Reactor.LargeBlockSmallGenerator
import spaceEngineers.util.generator.map.BlockPlacementInformation
import spaceEngineers.util.generator.maze.Direction

typealias AgentId = String
typealias ButtonId = String
typealias DoorId = String

fun cellFromText(text: String): LabRecruitCell? {
    return when (text) {
        "f", "|f" -> null // Floor
        "w", "|w" -> Wall
        else -> if (text.matches(BUTTON_REGEX)) {
            Button(BUTTON_REGEX.replace(text, "$1"))
        } else if (text.matches(AGENT_REGEX)) {
            Agent(AGENT_REGEX.replace(text, "$1"))
        } else if (text.matches(DOOR_REGEX)) {
            Door(DOOR_REGEX.replace(text, "$2"), DOOR_REGEX.replace(text, "$1").toDirection())
        } else {
            error("No cell for $text")
        }
    }
}

fun String.toDirection(): Direction {
    return when (this) {
        "n", "s" -> Direction.UP
        "e", "w" -> Direction.LEFT
        else -> error("Unknown direction for $this")
    }
}

sealed class LabRecruitCell : BlockPlacementInformation {
    abstract val regex: Regex
    open val charRepresentation: Char = this::class.simpleName?.first() ?: '?'
    override val orientationForward: Vec3I = Vec3I.FORWARD
    override val orientationUp: Vec3I = Vec3I.UP
    override fun toString(): String {
        return charRepresentation.toString()
    }

    override val color: Vec3F? = null
    override val offset: Vec3I = Vec3I.ZERO
}


object Wall : LabRecruitCell() {
    override val regex: Regex = "\\|?w".toRegex()
    override val blockId: DefinitionId = LargeHeavyBlockArmorBlock
    override val customName: String? = null
}

val BUTTON_REGEX = "f:b\\^(.*)".toRegex()
val AGENT_REGEX = "f:a\\^(.*)".toRegex()
val DOOR_REGEX = "f:d>([nwes])\\^(.*)".toRegex()

object Floor : LabRecruitCell() {
    override val regex: Regex = "\\|?f".toRegex()
    override val charRepresentation: Char = ' '
    override val blockId: DefinitionId = LargeHeavyBlockArmorBlock
    override val customName: String? = null
}

object Generator : LabRecruitCell() {
    override val regex: Regex = "\\|?g".toRegex()
    override val blockId: DefinitionId = LargeBlockSmallGenerator
    override val customName: String = blockId.id

}

object GravityGenerator : LabRecruitCell() {
    override val regex: Regex = "\\|?G".toRegex()
    override val blockId: DefinitionId = DefinitionIds.GravityGenerator.EMPTY
    override val customName: String = blockId.id

}

data class Button(val id: ButtonId) : LabRecruitCell() {
    override val regex: Regex = BUTTON_REGEX
    override val blockId: DefinitionId = LargeSciFiButtonTerminal
    override val customName: String = id
    override val orientationForward: Vec3I = Vec3I.BACKWARD
}

data class Agent(val id: AgentId) : LabRecruitCell() {
    override val regex: Regex = AGENT_REGEX
    override val blockId: DefinitionId = LargeMedicalRoom
    override val customName: String = id
    override val orientationForward: Vec3I = Vec3I.FORWARD
    override val orientationUp: Vec3I = Vec3I.UP
    override val offset: Vec3I = Vec3I(-1, 1, 0)
}

data class Door(val id: DoorId, val orientation: Direction) : LabRecruitCell() {
    override val regex: Regex = DOOR_REGEX
    override val orientationForward: Vec3I
        get() = when (orientation) {
            Direction.RIGHT -> Vec3I.RIGHT
            Direction.LEFT -> Vec3I.RIGHT
            Direction.UP -> Vec3I.FORWARD
            Direction.DOWN -> Vec3I.BACKWARD
        }
    override val blockId: DefinitionId = LargeBlockSlideDoor
    override val customName: String = id
}
