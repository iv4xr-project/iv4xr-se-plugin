package spaceEngineers.transport

import kotlin.test.Test
import kotlin.test.assertEquals

class AlwaysReturnSameLineReaderWriterTest {
    @Test
    fun `AlwaysReturnSameLineReaderWriter sends and receives the same line`() {
        val response = "foo"
        val readerWriter = AlwaysReturnSameLineReaderWriter(response)

        val result = readerWriter.sendAndReceiveLine("bar")

        assertEquals(response, result)
    }

    @Test
    fun `AlwaysReturnSameLineReaderWriter can be closed`() {
        val readerWriter = AlwaysReturnSameLineReaderWriter("foo")

        readerWriter.close()

        // No exception is thrown
    }
}
