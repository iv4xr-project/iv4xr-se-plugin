package spaceEngineers.controller.connection

import spaceEngineers.controller.ContextControllerWrapper

data class ProcessWithConnection(
    val spaceEngineers: ContextControllerWrapper,
    val gameProcess: GameProcess,
)
