package spaceEngineers.controller

import spaceEngineers.transport.AlwaysReturnSameLineReaderWriter
import spaceEngineers.transport.PresetLinesReaderWriter
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.StringLineReaderWriter
import java.io.File

class SpaceEngineersJavaProxyBuilder : JsonRpcSpaceEngineersBuilder {

    override fun localhost(agentId: String): SpaceEngineers {
        return fromStringLineReaderWriter(
            agentId = agentId,
            stringLineReaderWriter = SocketReaderWriter()
        )
    }

    override fun mock(agentId: String, file: File): SpaceEngineers {
        val text = file.readText().trimEnd()
        val lines = text.lines()
        return if (lines.size > 1) {
            mock(agentId = agentId, lines = lines)
        } else {
            mock(agentId = agentId, lineToReturn = lines.first())
        }
    }

    override fun mock(agentId: String, lineToReturn: String): SpaceEngineers {
        return fromStringLineReaderWriter(
            agentId = agentId,
            stringLineReaderWriter = AlwaysReturnSameLineReaderWriter(lineToReturn)
        )
    }

    override fun mock(agentId: String, lines: List<String>): SpaceEngineers {
        return fromStringLineReaderWriter(
            agentId = agentId,
            stringLineReaderWriter = PresetLinesReaderWriter(lines)
        )
    }

    override fun fromStringLineReaderWriter(
        agentId: String,
        stringLineReaderWriter: StringLineReaderWriter
    ): SpaceEngineers {
        return SpaceEngineersJavaProxy.fromStringLineReaderWriter(agentId, stringLineReaderWriter)
    }
}
