package spaceEngineers.transport

import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_HOSTNAME
import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_PORT
import java.io.IOException


class ReconnectingSocketReaderWriter @JvmOverloads constructor(
    val host: String = DEFAULT_HOSTNAME,
    val port: Int = DEFAULT_PORT,
    val maxWaitTimeMs: Int = 120_000,
    val socketConnectionTimeoutMs: Int = 4_000,
    val socketDataTimeoutMs: Int = 120_000,
    val maxRetries: Int = 3,
) : AutoCloseable, StringLineReaderWriter {

    var socketReaderWriter: SocketReaderWriter = connect()

    private fun connect(): SocketReaderWriter {
        return SocketReaderWriter(host, port, maxWaitTimeMs, socketConnectionTimeoutMs, socketDataTimeoutMs)
    }

    private fun reconnect() {
        close()
        socketReaderWriter = connect()
    }

    override fun sendAndReceiveLine(line: String): String {
        var exception: IOException? = null
        repeat(1 + maxRetries) {
            try {
                return socketReaderWriter.sendAndReceiveLine(line)
            } catch (e: IOException) {
                exception = e
                reconnect()
                socketReaderWriter.sendAndReceiveLine(line)
            }
        }
        throw exception!!
    }

    override fun close() {
        socketReaderWriter.close()
    }

}
