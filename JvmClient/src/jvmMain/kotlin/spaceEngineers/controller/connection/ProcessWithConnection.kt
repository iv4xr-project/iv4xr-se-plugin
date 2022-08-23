package spaceEngineers.controller.connection

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SpaceEngineers

data class ProcessWithConnection(
    val spaceEngineers: ContextControllerWrapper,
    val gameProcess: GameProcess,
) : SpaceEngineers by spaceEngineers {

    val context = spaceEngineers.context
    val contextControllerWrapper = spaceEngineers
}
