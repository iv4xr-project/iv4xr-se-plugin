package spaceEngineers.transport

import kotlinx.coroutines.runBlocking
import java.io.*
import java.net.ServerSocket
import java.net.Socket


fun main() {
    HttpProxy().start()
}

class HttpProxy(val httpPort: UShort = 9888U, val tcpPort: UShort = 3333u, val socketDataTimeoutMs: Int = 20000) {

    fun start() = runBlocking {
        val socket = ServerSocket(httpPort.toInt())
        //socket.soTimeout = socketDataTimeoutMs
        while (true) {
            processSingleRequest(socket.accept())
        }
    }

    private fun processSingleRequest(clientSocket: Socket) {
        clientSocket.use { socket ->
            val reader = BufferedReader(InputStreamReader(socket.getInputStream(), Charsets.UTF_8))
            val writer =
                PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream(), Charsets.UTF_8)), true)
            var line: String? = "-"
            while (line != null && line.isNotEmpty()) {
                line = reader.readLine()
            }
            writer.println(processSingleLine(reader.readLine()))
        }
    }

    private fun processSingleLine(line: String): String {
        SocketReaderWriter(port = tcpPort.toInt()).use { socketReaderWriter ->
            val result = socketReaderWriter.sendAndReceiveLine(line.trim())
            return """
HTTP/1.1 200 OK
Access-Control-Allow-Origin: *
Content-type: application/json

$result
            """.trimIndent() + "\n"
        }
    }

}
