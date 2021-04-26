package spaceEngineers.transport

import com.google.gson.Gson
import environments.SocketReaderWriter
import environments.closeIfCloseable
import spaceEngineers.SeRequest

open class GsonReaderWriter(
    val gson: Gson = SocketReaderWriter.SPACE_ENG_GSON,
    val stringLineReaderWriter: StringLineReaderWriter = SocketReaderWriter()
) : AutoCloseable {


    fun <T> processRequest(request: SeRequest<T>): T {
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(gson.toJson(request))
        return gson.fromJson(responseJson, request.responseType)
    }

    override fun close() {
        stringLineReaderWriter.closeIfCloseable()
    }

}
