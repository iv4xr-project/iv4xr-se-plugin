package spaceEngineers.transport

import java.io.File

class GsonResponseWriteToFileReaderWriter(override val rw: StringLineReaderWriter, val file: File) : StringLineReaderWrapper,
    AutoCloseable {
    override fun sendAndReceiveLine(line: String): String {
        return rw.sendAndReceiveLine(line).also { responseLine ->
            file.writeText(responseLine)
        }
    }

    override fun close() {
        rw.closeIfCloseable()
    }

}
