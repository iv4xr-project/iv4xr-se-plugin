package spaceEngineers.util.generator.map

import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LabRecruitsMapTest {

    val mapsDir = File("./src/jvmTest/resources/labrecruits/maps/")

    private fun File.toMap(): LabRecruitsMap {
        return LabRecruitsMap.fromString(readText())
    }

    @Test
    fun ensureEmptyCommasAndDifferentNamesWork() {
        val originalMapsDir = File(mapsDir, "original")
        val originalMapText = File(originalMapsDir, "button1_opens_door1.csv").toMap()
        val fixedMapText = File(originalMapsDir, "button1_opens_door1_fixed_for_se.csv").toMap()
        assertEquals(1, originalMapText.doorsByButtonId("button1").size)
        assertEquals(1, fixedMapText.doorsByButtonId("b1").size)
    }

    @Test
    fun loadAndSaveTest() {
        mapsDir.listFiles { pathname -> pathname.extension == "csv" }
            .forEach {
                checkMap(it.name)
            }
    }

    private fun checkMap(name: String) {
        println(name)
        val text = File(mapsDir, name).readText()
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
