package spaceEngineers.transport

import java.io.File

class GsonResponseAppendToFileReaderWriter(
    override val rw: StringLineReaderWriter,
    val file: File,
    clearFile: Boolean = false
) : StringLineReaderWrapper,
    AutoCloseable {

    init {
        if (clearFile) {
            file.writeText("")
        }
    }


    override fun sendAndReceiveLine(line: String): String {
        return rw.sendAndReceiveLine(line).also { responseLine ->
            file.appendText(responseLine + "\n")
        }
    }

    override fun close() {
        rw.closeIfCloseable()
    }

}
