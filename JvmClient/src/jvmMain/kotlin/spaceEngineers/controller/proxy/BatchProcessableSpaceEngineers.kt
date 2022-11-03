package spaceEngineers.controller.proxy

import spaceEngineers.controller.SpaceEngineers

class BatchProcessableSpaceEngineers(
    val spaceEngineers: SpaceEngineers,
    val rpcCaller: JsonRpcCaller,
    val handler: SpaceEngineersBatchJavaProxy,
) : SpaceEngineers by spaceEngineers
