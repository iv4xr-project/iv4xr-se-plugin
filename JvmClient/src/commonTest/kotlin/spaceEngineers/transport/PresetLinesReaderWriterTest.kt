package spaceEngineers.transport

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PresetLinesReaderWriterTest {

    @Test
    fun testSendAndReceiveLineReturnsExpectedLine() {
        val lines = listOf("line1", "line2", "line3")
        val reader = PresetLinesReaderWriter(lines)
        assertEquals("line1", reader.sendAndReceiveLine(""))
        assertEquals("line2", reader.sendAndReceiveLine(""))
        assertEquals("line3", reader.sendAndReceiveLine(""))
    }

    @Test
    fun testSendAndReceiveLineThrowsIndexOutOfBoundsException() {
        val lines = listOf("line1", "line2")
        val reader = PresetLinesReaderWriter(lines)
        reader.sendAndReceiveLine("")
        reader.sendAndReceiveLine("")
        assertFailsWith<IndexOutOfBoundsException> {
            reader.sendAndReceiveLine("")
        }
    }

    @Test
    fun testSendAndReceiveLineDoesNotModifyOriginalLinesList() {
        val lines = listOf("line1", "line2")
        val reader = PresetLinesReaderWriter(lines)
        reader.sendAndReceiveLine("")
        assertEquals(listOf("line1", "line2"), lines)
    }

    @Test
    fun testSendAndReceiveLineIncrementsIndex() {
        val lines = listOf("line1", "line2")
        val reader = PresetLinesReaderWriter(lines)
        reader.sendAndReceiveLine("")
        assertEquals(1, reader.index)
    }
}
