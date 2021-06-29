package spaceEngineers.transport

import com.google.gson.Gson

open class GsonReaderWriter(
    val gson: Gson = SocketReaderWriter.SPACE_ENG_GSON,
    val stringLineReaderWriter: StringLineReaderWriter = SocketReaderWriter()
) : AutoCloseable {

    override fun close() {
        stringLineReaderWriter.closeIfCloseable()
    }

}
