package spaceEngineers.transport

interface StringLineReaderWriter: Closeable {
    fun sendAndReceiveLine(line: String): String
}

interface StringLineReaderWrapper: StringLineReaderWriter {
    val rw: StringLineReaderWriter
}
