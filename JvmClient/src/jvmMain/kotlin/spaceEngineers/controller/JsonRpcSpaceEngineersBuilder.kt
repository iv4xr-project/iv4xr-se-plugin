package spaceEngineers.controller

import java.io.File

interface JsonRpcSpaceEngineersBuilder {

    fun localhost(agentId: String): JsonRpcSpaceEngineers

    fun mock(agentId: String, file: File): JsonRpcSpaceEngineers

    fun mock(agentId: String, lineToReturn: String): JsonRpcSpaceEngineers

    fun mock(agentId: String, lines: List<String>): JsonRpcSpaceEngineers


    companion object : JsonRpcSpaceEngineersBuilder {

        var builder: JsonRpcSpaceEngineersBuilder = KotlinJsonRpcSpaceEngineersBuilder()

        override fun localhost(agentId: String): JsonRpcSpaceEngineers {
            return builder.localhost(agentId)
        }

        override fun mock(agentId: String, file: File): JsonRpcSpaceEngineers {
            return builder.mock(agentId, file)
        }

        override fun mock(agentId: String, lineToReturn: String): JsonRpcSpaceEngineers {
            return builder.mock(agentId, lineToReturn)
        }

        override fun mock(agentId: String, lines: List<String>): JsonRpcSpaceEngineers {
            return builder.mock(agentId, lines)
        }

    }
}
