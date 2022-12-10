package spaceEngineers.transport

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class AppendToFileReaderWriterTest {

    @Test
    fun testAppendToFileReaderWriter() {
        val file = File.createTempFile("append-to-file-reader-writer-test", ".tmp").apply {
            deleteOnExit()
        }

        val readerWriter = object : StringLineReaderWriter {
            override fun sendAndReceiveLine(line: String): String {
                return "Test response for line: $line"
            }
        }

        val appendToFileReaderWriter = AppendToFileReaderWriter(readerWriter, file, true)

        val response = appendToFileReaderWriter.sendAndReceiveLine("Test line")
        assertEquals("Test response for line: Test line", response)

        val fileContent = file.readText()
        assertEquals("Test response for line: Test line\n", fileContent)
    }

    @Test
    fun testAppendToFileReaderWriter_withExistingFile_clearFileFalse() {
        // Create a temporary file and write some text to it
        val file = File.createTempFile("append-to-file-reader-writer-test", ".tmp").apply {
            deleteOnExit()
        }
        file.writeText("This is some existing text in the file.\n")

        // Create a StringLineReaderWriter that we can use to test the AppendToFileReaderWriter
        val readerWriter = object : StringLineReaderWriter {
            override fun sendAndReceiveLine(line: String): String {
                return "Test response for line: $line"
            }
        }

        val appendToFileReaderWriter = AppendToFileReaderWriter(readerWriter, file, false)

        val response = appendToFileReaderWriter.sendAndReceiveLine("Test line")
        assertEquals("Test response for line: Test line", response)

        val fileContent = file.readText()
        assertEquals("This is some existing text in the file.\nTest response for line: Test line\n", fileContent)
    }

    @Test
    fun testAppendToFileReaderWriter_withNonExistingFile() {
        // Create a temporary file
        val file = File.createTempFile("append-to-file-reader-writer-test", ".tmp").apply {
            deleteOnExit()
        }
        file.delete()

        // Create a StringLineReaderWriter that we can use to test the AppendToFileReaderWriter
        val readerWriter = object : StringLineReaderWriter {
            override fun sendAndReceiveLine(line: String): String {
                return "Test response for line: $line"
            }
        }

        val appendToFileReaderWriter = AppendToFileReaderWriter(readerWriter, file, true)

        val response = appendToFileReaderWriter.sendAndReceiveLine("Test line")
        assertEquals("Test response for line: Test line", response)

        val fileContent = file.readText()
        assertEquals("Test response for line: Test line\n", fileContent)
    }
}
