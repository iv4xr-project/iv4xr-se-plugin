package spaceEngineers.transport

class PresetLinesReaderWriter(val lines: List<String>) : StringLineReaderWriter {

    var index = 0

    override fun sendAndReceiveLine(line: String): String {
        if (index > lines.lastIndex ) {
            throw IndexOutOfBoundsException("Reader ran out of lines: Index $index out of bounds for length ${lines.size}")
        }
        return lines[index++]
    }
}
