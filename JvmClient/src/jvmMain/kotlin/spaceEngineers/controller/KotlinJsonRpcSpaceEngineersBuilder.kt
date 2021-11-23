package spaceEngineers.controller

import spaceEngineers.transport.AlwaysReturnSameLineReaderWriter
import spaceEngineers.transport.PresetLinesReaderWriter
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.StringLineReaderWriter
import java.io.File

class KotlinJsonRpcSpaceEngineersBuilder: JsonRpcSpaceEngineersBuilder {

    override fun localhost(agentId: String): JsonRpcSpaceEngineers {
        return JsonRpcSpaceEngineers(
            agentId = agentId,
            stringLineReaderWriter = SocketReaderWriter()
        )
    }

    override fun mock(agentId: String, file: File): JsonRpcSpaceEngineers {
        val text = file.readText().trimEnd()
        val lines = text.lines()
        return if (lines.size > 1) {
            mock(agentId = agentId, lines = lines)
        } else {
            mock(agentId = agentId, lineToReturn = lines.first())
        }
    }

    override fun mock(agentId: String, lineToReturn: String): JsonRpcSpaceEngineers {
        return JsonRpcSpaceEngineers(
            agentId = agentId,
            stringLineReaderWriter = AlwaysReturnSameLineReaderWriter(lineToReturn)
        )
    }

    override fun mock(agentId: String, lines: List<String>): JsonRpcSpaceEngineers {
        return JsonRpcSpaceEngineers(
            agentId = agentId,
            stringLineReaderWriter = PresetLinesReaderWriter(lines)
        )
    }

    override fun fromStringLineReaderWriter(
        agentId: String,
        stringLineReaderWriter: StringLineReaderWriter
    ): JsonRpcSpaceEngineers {
        return JsonRpcSpaceEngineers(agentId, stringLineReaderWriter)
    }
}
