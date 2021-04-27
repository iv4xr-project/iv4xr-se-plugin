package spaceEngineers.transport

class AlwaysReturnSameLineReaderWriter(val response: String) : StringLineReaderWriter {

    override fun sendAndReceiveLine(line: String): String {
        return response
    }
}