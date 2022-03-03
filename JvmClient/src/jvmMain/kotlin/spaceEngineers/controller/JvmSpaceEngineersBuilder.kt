package spaceEngineers.controller

class JvmSpaceEngineersBuilder {

    companion object {
        fun default(): JsonRpcSpaceEngineersBuilder {
            return SpaceEngineersJavaProxyBuilder()
        }
    }
}
