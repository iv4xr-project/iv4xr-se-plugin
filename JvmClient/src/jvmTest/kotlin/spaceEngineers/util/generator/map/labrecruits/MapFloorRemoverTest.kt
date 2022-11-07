package spaceEngineers.util.generator.map.labrecruits

import spaceEngineers.util.generator.map.toSimplified
import kotlin.test.Test
import kotlin.test.assertEquals

class MapFloorRemoverTest {

    fun map(text: String): LabRecruitsMap {
        return LabRecruitsMap.fromString(text.trim())
    }

    @Test
    fun test1x2() {
        val mapStr = """
|f,f,f,f,f
w,w,w,w,w
f,f,f,f,f:d>n^door0
w,w,w,w,w
        """.trim()
        val map = map(mapStr)
        assertEquals(map.cells[0][0], Floor)
        val simpleMap = map.toSimplified()
        assertEquals(simpleMap.cells[0][0], null)
    }

}
