package environments

import spaceEngineers.transport.StringLineReaderWriter
import java.io.RandomAccessFile


fun RandomAccessFile.println(text: String) {
    write("$text\r\n".toByteArray(charset("UTF-8")))
}

class NamedPipeReaderWriter(val name: String) : StringLineReaderWriter, AutoCloseable {

    val pipe = RandomAccessFile("\\\\.\\pipe\\${name}", "rw")


    override fun sendAndReceiveLine(line: String): String {
        println(line)
        pipe.println(line)
        return pipe.readLine().apply(::println)
    }

    override fun close() {
        pipe.close()
    }
}
