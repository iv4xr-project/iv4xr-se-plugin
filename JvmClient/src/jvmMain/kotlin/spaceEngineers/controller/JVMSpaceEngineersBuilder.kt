package spaceEngineers.controller

class JVMSpaceEngineersBuilder {

    companion object {
        fun default(): JsonRpcSpaceEngineersBuilder {
            return SpaceEngineersJavaProxyBuilder()
        }
    }
}
