package spaceEngineers.util.generator.map

import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LabRecruitsMapTest {

    @Test
    fun loadAndSaveTest() {
        File("./src/jvmTest/resources/labrecruits/maps/").listFiles { pathname -> pathname.extension == "csv" }
            .forEach {
                checkMap(it.name)
            }

    }

    private fun checkMap(name: String) {
        println(name)
        val text = File("./src/jvmTest/resources/labrecruits/maps/$name").readText()
        val labRecruitsMap = LabRecruitsMap.fromString(text)
        val newText = labRecruitsMap.toCsv()
        verifyMaps(text, newText)

    }

    private fun verifyMaps(mapText: String, anotherMapText: String) {
        val lines = mapText.trim().lines()
        val newLines = anotherMapText.trim().lines()
        assertTrue(lines.size >= newLines.size)
        newLines.indices.forEach { index ->
            assertEquals(lines[index], newLines[index])
        }
    }
}
