package spaceEngineers.transport

class PresetLinesReaderWriter(val lines: List<String>) : StringLineReaderWriter {

    var index = 0

    override fun sendAndReceiveLine(line: String): String {
        return lines[index++]
    }
}
