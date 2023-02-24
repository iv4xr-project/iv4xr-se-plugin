package bdd.connection

import bdd.connection.GameProcess
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.controller.extend
import spaceEngineers.iv4xr.navigation.Iv4XRAStarPathFinder
import spaceEngineers.model.BlockId
import spaceEngineers.model.Vec3F
import spaceEngineers.navigation.PathFinder

data class ProcessWithConnection(
    val spaceEngineers: ContextControllerWrapper,
    val gameProcess: GameProcess,
    val pathFinder: PathFinder<BlockId, Vec3F, String, String> = Iv4XRAStarPathFinder(),
) : ExtendedSpaceEngineers by spaceEngineers.extend(pathFinder) {

    val context = spaceEngineers.context
    val contextControllerWrapper = spaceEngineers
}
