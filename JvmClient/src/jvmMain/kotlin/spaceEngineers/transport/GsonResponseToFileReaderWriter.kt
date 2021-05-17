package spaceEngineers.transport

import java.io.File

class GsonResponseToFileReaderWriter(val rw: StringLineReaderWriter, val file: File) : StringLineReaderWriter,
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
