package spaceEngineers.controller

class JvmSpaceEngineersBuilder {

    companion object {
        //@JvmStatic
        fun default(): JsonRpcSpaceEngineersBuilder {
            return SpaceEngineersJavaProxyBuilder()
        }

        fun foo() : Int { return 10 }
    }
}
