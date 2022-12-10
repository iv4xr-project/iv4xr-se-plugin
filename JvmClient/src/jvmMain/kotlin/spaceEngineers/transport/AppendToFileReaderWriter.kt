package spaceEngineers.transport

import java.io.File

class AppendToFileReaderWriter(
    override val rw: StringLineReaderWriter,
    val file: File,
    clearFile: Boolean = true
) : StringLineReaderWrapper, Closeable by rw {

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
}
