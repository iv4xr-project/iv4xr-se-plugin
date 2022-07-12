package spaceEngineers.transport

interface StringLineReaderWriter {
    fun sendAndReceiveLine(line: String): String
}

interface StringLineReaderWrapper: StringLineReaderWriter {
    val rw: StringLineReaderWriter
}
