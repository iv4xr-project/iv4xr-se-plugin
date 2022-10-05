package spaceEngineers.controller.connection

import spaceEngineers.controller.*

data class ProcessWithConnection(
    val spaceEngineers: ContextControllerWrapper,
    val gameProcess: GameProcess,
) : ExtendedSpaceEngineers by DataExtendedSpaceEngineers(spaceEngineers) {

    val context = spaceEngineers.context
    val contextControllerWrapper = spaceEngineers
}
