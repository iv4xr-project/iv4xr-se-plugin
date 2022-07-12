package spaceEngineers.util.generator.map.labrecruits

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
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
            Agent(BUTTON_REGEX.replace(text, "$1"))
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
    abstract fun fromText(text: String): LabRecruitCell
    open val charRepresentation: Char = this::class.simpleName?.first() ?: '?'
    override val orientationForward: Vec3I = Vec3I.UP
    override val orientationUp: Vec3I = Vec3I.BACKWARD
    override fun toString(): String {
        return charRepresentation.toString()
    }

    override val color: Vec3F? = null
}


object Wall : LabRecruitCell() {
    override val regex: Regex = "\\|?w".toRegex()
    override fun fromText(text: String): LabRecruitCell = Wall
    override val blockId: DefinitionId = DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock")
}

val BUTTON_REGEX = "f:b\\^(.*)".toRegex()
val AGENT_REGEX = "f:a\\^(.*)".toRegex()
val DOOR_REGEX = "f:d>([nwes])\\^(.*)".toRegex()

object Floor : LabRecruitCell() {
    override val regex: Regex = "\\|?f".toRegex()
    override fun fromText(text: String): LabRecruitCell = Floor
    override val charRepresentation: Char = ' '
    override val blockId: DefinitionId = DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock")
}

data class Button(val id: ButtonId) : LabRecruitCell() {
    override val regex: Regex = BUTTON_REGEX
    override fun fromText(text: String): LabRecruitCell {
        return Button(regex.replace(text, "$1"))
    }

    override val blockId: DefinitionId = DefinitionId.reactor("LargeBlockSmallGenerator")
}

data class Agent(val id: AgentId) : LabRecruitCell() {
    override val regex: Regex = AGENT_REGEX
    override fun fromText(text: String): LabRecruitCell {
        return Agent(regex.replace(text, "$1"))
    }

    override val blockId: DefinitionId = DefinitionId.reactor("LargeBlockSmallGenerator")
}

data class Door(val id: DoorId, val orientation: Direction) : LabRecruitCell() {
    override val regex: Regex = DOOR_REGEX
    override fun fromText(text: String): LabRecruitCell {
        return Door(regex.replace(text, "$2"), regex.replace(text, "$1").toDirection())
    }

    override val orientationForward: Vec3I
        get() = when (orientation) {
            Direction.RIGHT -> Vec3I.RIGHT
            Direction.LEFT -> Vec3I.LEFT
            Direction.UP -> Vec3I.UP
            Direction.DOWN -> Vec3I.UP
        }
    override val blockId: DefinitionId = DefinitionId.door("LargeBlockSlideDoor")
}
