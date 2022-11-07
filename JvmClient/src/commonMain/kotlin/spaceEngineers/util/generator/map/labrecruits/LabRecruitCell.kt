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
import spaceEngineers.util.generator.map.Orientations
import spaceEngineers.util.generator.maze.Direction

typealias AgentId = String
typealias ButtonId = String
typealias DoorId = String

fun cellFromText(text: String): LabRecruitCell? {
    return when (text) {
        "f", "|f" -> Floor
        "w", "|w" -> Wall
        else -> if (text.matches(BUTTON_REGEX)) {
            Button(BUTTON_REGEX.replace(text, "$1"))
        } else if (text.matches(AGENT_REGEX)) {
            Agent(AGENT_REGEX.replace(text, "$1"))
        } else if (text.matches(DOOR_REGEX)) {
            Door(DOOR_REGEX.replace(text, "$2"), DOOR_REGEX.replace(text, "$1").first().toDirection())
        } else {
            error("No cell for $text")
        }
    }
}

sealed class LabRecruitCell : BlockPlacementInformation {
    abstract val regex: Regex
    abstract fun toCsv(): String
    override val orientations: List<Orientations> = listOf(Orientations())
    override val color: Vec3F? = null
    override val offset: Vec3I = Vec3I.ZERO
}


object Wall : LabRecruitCell() {
    override val regex: Regex = "\\|?w".toRegex()
    override fun toCsv(): String {
        return "w"
    }

    override val blockId: DefinitionId = LargeHeavyBlockArmorBlock
    override val customName: String? = null
}

val BUTTON_REGEX = "^f:b\\^(b[0-9]+)$".toRegex()
val AGENT_REGEX = "^f:a\\^(Agent[0-9]+)$".toRegex()
val DOOR_REGEX = "^f:d>([nwes])\\^(door[0-9]+)$".toRegex()

object Floor : LabRecruitCell() {
    override val regex: Regex = "\\|?f".toRegex()
    override val charRepresentation: Char = ' '
    override val blockId: DefinitionId = LargeHeavyBlockArmorBlock
    override val customName: String? = null
    override fun toCsv(): String {
        return "f"
    }
}

object UnnecessaryFloor : LabRecruitCell() {
    override val regex: Regex = "\\|?f".toRegex()
    override val charRepresentation: Char = ' '
    override val blockId: DefinitionId = DefinitionIds.CubeBlock.Catwalk
    override val customName: String? = null
    override fun toCsv(): String {
        return "f"
    }
}


object Generator : LabRecruitCell() {
    override val regex: Regex = "\\|?g".toRegex()
    override fun toCsv(): String {
        return "g"
    }

    override val blockId: DefinitionId = LargeBlockSmallGenerator
    override val customName: String = blockId.id

}

object GravityGenerator : LabRecruitCell() {
    override val regex: Regex = "\\|?G".toRegex()
    override fun toCsv(): String {
        return "G"
    }

    override val blockId: DefinitionId = DefinitionIds.GravityGenerator.EMPTY
    override val customName: String = blockId.id
}

data class Button(val id: ButtonId) : LabRecruitCell() {
    override val regex: Regex = BUTTON_REGEX
    override val blockId: DefinitionId = LargeSciFiButtonTerminal
    override val customName: String = id
    override val orientations: List<Orientations> = listOf(
        Orientations(forward = Vec3I.FORWARD),
        Orientations(forward = Vec3I.BACKWARD),
        Orientations(forward = Vec3I.LEFT),
        Orientations(forward = Vec3I.RIGHT),
    )

    override fun toCsv(): String {
        return "f:b^${id}"
    }

    override val color: Vec3F = Vec3F.GREEN
}

data class Agent(val id: AgentId) : LabRecruitCell() {
    override val regex: Regex = AGENT_REGEX
    override val blockId: DefinitionId = LargeMedicalRoom
    override val customName: String = id
    override val orientations: List<Orientations> = listOf(Orientations())
    override val offset: Vec3I = Vec3I(-1, 1, 0)
    override fun toCsv(): String {
        return "f:a^${id}"
    }

    override val color: Vec3F = Vec3F.BLUE
}

data class Door(val id: DoorId, val orientation: Direction) : LabRecruitCell() {
    override val regex: Regex = DOOR_REGEX
    override val orientations: List<Orientations> = listOf(
        Orientations(forward = orientation.toVec3I())
    )
    override val blockId: DefinitionId = LargeBlockSlideDoor
    override val customName: String = id

    override fun toCsv(): String {
        return "f:d>${orientation.toChar()}^${id}"
    }

    override val color: Vec3F = Vec3F.RED
}
