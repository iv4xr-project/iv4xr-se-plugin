package spaceEngineers.controller

import spaceEngineers.controller.SpaceEngineers.Companion.DEFAULT_AGENT_ID
import spaceEngineers.transport.StringLineReaderWriter
import java.io.File

interface JsonRpcSpaceEngineersBuilder {

    fun localhost(agentId: String = DEFAULT_AGENT_ID): SpaceEngineers

    fun mock(agentId: String = DEFAULT_AGENT_ID, file: File): SpaceEngineers

    fun mock(agentId: String = DEFAULT_AGENT_ID, lineToReturn: String): SpaceEngineers

    fun mock(agentId: String = DEFAULT_AGENT_ID, lines: List<String>): SpaceEngineers

    fun fromStringLineReaderWriter(agentId: String, stringLineReaderWriter: StringLineReaderWriter): SpaceEngineers

    companion object : JsonRpcSpaceEngineersBuilder {

        var builder: JsonRpcSpaceEngineersBuilder = KotlinJsonRpcSpaceEngineersBuilder()

        override fun localhost(agentId: String): SpaceEngineers {
            return builder.localhost(agentId)
        }

        override fun mock(agentId: String, file: File): SpaceEngineers {
            return builder.mock(agentId, file)
        }

        override fun mock(agentId: String, lineToReturn: String): SpaceEngineers {
            return builder.mock(agentId, lineToReturn)
        }

        override fun mock(agentId: String, lines: List<String>): SpaceEngineers {
            return builder.mock(agentId, lines)
        }

        override fun fromStringLineReaderWriter(agentId: String, stringLineReaderWriter: StringLineReaderWriter): SpaceEngineers {
            return builder.fromStringLineReaderWriter(agentId, stringLineReaderWriter)
        }

    }
}
