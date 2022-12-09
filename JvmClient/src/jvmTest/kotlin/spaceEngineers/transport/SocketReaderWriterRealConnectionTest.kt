package spaceEngineers.transport

import java.io.*
import java.net.*
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread
import kotlin.test.Test
import kotlin.test.assertEquals

class SocketReaderWriterRealConnectionTest {

    @Test
    fun testSendAndReceiveLine() {
        val server = ServerSocket(0)

        val serverThread = thread(start = true) {
            val client = server.accept()
            val reader = BufferedReader(InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8))
            val writer = PrintWriter(OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true)
            var line = reader.readLine()
            while (line != null) {
                writer.println("received: $line")
                line = reader.readLine()
            }
            client.close()
            server.close()
        }

        val socketReaderWriter = SocketReaderWriter(host = "localhost", port = server.localPort.toUShort())

        val response = socketReaderWriter.sendAndReceiveLine("hello")
        assertEquals("received: hello", response)
        socketReaderWriter.close()

        serverThread.join()
    }
}
