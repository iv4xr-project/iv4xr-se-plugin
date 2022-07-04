package spaceEngineers.util.generator.map

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3I

interface BlockPlacementInformation {
    val blockId: DefinitionId
    val orientationForward: Vec3I
    val orientationUp: Vec3I
}

