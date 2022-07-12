package spaceEngineers.util.generator.map

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I

interface BlockPlacementInformation {
    val blockId: DefinitionId
    val orientationForward: Vec3I
    val orientationUp: Vec3I
    val color: Vec3F?
}

data class DataBlockPlacementInformation(
    override val blockId: DefinitionId,
    override val orientationForward: Vec3I,
    override val orientationUp: Vec3I,
    override val color: Vec3F? = null,
) : BlockPlacementInformation
