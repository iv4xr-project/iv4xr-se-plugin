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
}
