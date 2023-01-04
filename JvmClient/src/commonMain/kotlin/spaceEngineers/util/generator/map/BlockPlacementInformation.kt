package spaceEngineers.util.generator.map

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.toInt
import spaceEngineers.movement.BasicDirection3d

interface BlockPlacementInformation {
    val blockId: DefinitionId
    val orientations: List<Orientations>
    val color: Vec3F?
    val customName: String?
    val offset: Vec3I
    val charRepresentation: Char
        get() = this::class.simpleName?.first() ?: '?'
}

val BlockPlacementInformation.orientationForward
    get() = orientations.first().forward

val BlockPlacementInformation.orientationUp
    get() = orientations.first().up

data class Orientations(
    val forward: Vec3I = Vec3I.FORWARD,
    val up: Vec3I = Vec3I.UP,
) {
    companion object {
        val ALL = BasicDirection3d.excludingNone().flatMap { forward ->
            forward.perpendiculars().map { up ->
                Orientations(up = up.vector.toInt(), forward = forward.vector.toInt())
            }
        }
    }
}

data class DataBlockPlacementInformation(
    override val blockId: DefinitionId,
    override val color: Vec3F? = null,
    override val customName: String? = null,
    override val offset: Vec3I = Vec3I.ZERO,
    override val orientations: List<Orientations> = listOf(Orientations()),
) : BlockPlacementInformation {
    init {
        check(orientations.isNotEmpty()) {
            "Has to be at least one orientations!"
        }
    }
}
