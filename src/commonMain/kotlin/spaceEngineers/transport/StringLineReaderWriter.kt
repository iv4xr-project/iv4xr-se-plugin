package spaceEngineers.transport

interface StringLineReaderWriter {
    fun sendAndReceiveLine(line: String): String
}