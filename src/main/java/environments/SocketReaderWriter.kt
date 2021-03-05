package environments

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import logger.PrintColor
import nl.uu.cs.aplib.mainConcepts.Environment
import spaceEngineers.SeRequest
import java.io.*
import java.lang.reflect.Modifier
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets


fun SocketReaderWriter.sendCommand_(cmd: Environment.EnvOperation): Any? {
    // The Environment super class uses sendCommand_ to send the json object
    val json = cmd.arg as String
    return when (cmd.command) {
        "debug" -> {
            println(json)
            null
        }
        "request" -> try {
            sendAndReceiveLine(json)
        } catch (ex: IOException) {
            println("I/O error: " + ex.message)
            null
        }
        else -> throw IllegalArgumentException("Unknown command ${cmd.command}")
    }
}

fun <T> SocketReaderWriter.processRequest(request: SeRequest<T>): T {
    val responseJson = sendAndReceiveLine(gson.toJson(request))
    return gson.fromJson(responseJson, request.responseType)
}

fun SocketReaderWriter.sendAndReceiveLine(line: String): String {
    //TODO: configure gson to ignore companion object
    writer.println(line.replace(",\"Companion\":{}", ""))
    return reader.readLine()
}

/**
 * Provide a reader/writer to a socket to communicate with the system under test.
 * This class only provides raw communication over socket, where we can send
 * and receive an object. When an object is sent, it will be serialized to
 * a Json string. Likewise, the system under test is assumed to send each response
 * object as a Json string.
 */
class SocketReaderWriter @JvmOverloads constructor(
    host: String, port: Int, maxWaitTimeMs: Int = 20000,
    socketConnectionTimeoutMs: Int = 4000,
    socketDataTimeoutMs: Int = 4000,
    val gson: Gson = DEFAULT_GSON
) : AutoCloseable {

    lateinit var socket: Socket
    lateinit var reader: BufferedReader
    lateinit var writer: PrintWriter

    init {
        println(
            String.format(
                "Trying to connect with %s on %s:%s (will time-out after %s seconds)",
                PrintColor.BLUE("Unity"),
                host,
                port,
                maxWaitTimeMs / 1000
            )
        )
        val startTime = System.nanoTime()
        var connected = false
        var exception: IOException? = null
        while (!connected && millisElapsed(startTime) < maxWaitTimeMs) {
            try {
                socket = Socket()
                socket.soTimeout = socketDataTimeoutMs
                socket.connect(InetSocketAddress(host, port), socketConnectionTimeoutMs)
                reader = BufferedReader(InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
                writer = PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)
                connected = true
            } catch (ignored: IOException) {
                exception = ignored
            }
        }
        exception?.let {
            if (!connected) {
                throw it
            }
        }
    }

    /**
     * Send an object to the system under test. The object will first be serialized
     * to a json string, so it is assumed that the json serializer knows how to
     * handle the object.
     */
    fun write(packageToSend: Any) {
        val json = gson.toJson(packageToSend)
        if (debug) {
            println("** SENDING: $json")
        }
        writer.println(json)
    }

    /**
     * Read an object that was sent by the system under test. The object will be
     * received as a json string, which is then converted into an instance of the
     * given class. It is assumed that the json deserializer knows how to do this.
     * The resulting object is then returned.
     */
    fun <T> read(expectedClassOfResultObj: Class<T>): T {
        val response = reader.readLine()
        // we do not have to cast to T, since req.responseType is of type Class<T>
        if (debug) {
            println("** RECEIVING: $response")
        }
        return gson.fromJson(response, expectedClassOfResultObj)
    }

    /**
     * Close the socket/reader/writer
     * @throws IOException
     */
    override fun close() {
        closeSafely(reader)
        closeSafely(writer)
        closeSafely(socket)
    }


    companion object {
        @JvmField
        var debug = false

        // Configuring the json serializer/deserializer. Register custom serializers
        // here.
        // Transient modifiers should be excluded, otherwise they will be send with json
        private val DEFAULT_GSON = GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create()

        private fun millisElapsed(startTimeNano: Long): Float {
            return (System.nanoTime() - startTimeNano) / 1000000f
        }

        fun closeSafely(closeable: Closeable?) {
            try {
                closeable?.close()
            } catch (e: IOException) {
                //at this point we don't care, just cleanup
            }
        }
    }

}