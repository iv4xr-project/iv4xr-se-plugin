package spaceEngineers.util.generator.map

import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import kotlin.test.Test
import kotlin.test.assertEquals

class SimplifiedMapTest {

    fun map(text: String): LabRecruitsMap {
        return LabRecruitsMap.fromString(text.trim())
    }

    @Test
    fun test1x1() {
        val mapStr = """
|w
        """.trim()

        val map = map(mapStr)
        assertEquals(map.width, 1)
        assertEquals(map.height, 1)
    }

    @Test
    fun test1x2() {
        val mapStr = """
|w,w
        """.trim()

        val map = map(mapStr)
        assertEquals(map.width, 2)
        assertEquals(map.height, 1)
    }

    @Test
    fun test1x2simplified() {
        val mapStr = """
|w,w
        """.trim()

        val map = map(mapStr).toSimplified()
        assertEquals(map.width, 1)
        assertEquals(map.height, 1)
    }

    @Test
    fun test1x3simplified() {
        val mapStr = """
|w,w,w
        """.trim()

        val map = map(mapStr).toSimplified()
        assertEquals(map.width, 1)
        assertEquals(map.height, 1)
    }

    @Test
    fun test1x4simplified() {
        val mapStr = """
|w,f,w
        """.trim()

        val map = map(mapStr).toSimplified()
        assertEquals(map.width, 3)
        assertEquals(map.height, 1)
    }

    @Test
    fun test1x5simplified() {
        val mapStr = """
|w,f,w,w
        """.trim()

        val map = map(mapStr).toSimplified()
        assertEquals(map.width, 3)
        assertEquals(map.height, 1)
    }

    @Test
    fun test2x4simplified() {
        val mapStr = """
|w,f,w,w
w,f,w,w
        """.trim()

        val map = map(mapStr).toSimplified()
        assertEquals(map.width, 3)
        assertEquals(map.height, 1)
    }

    @Test
    fun test2x4simplifiedV2() {
        val mapStr = """
|w,f,w,w
w,f,w,f
        """.trim()

        val map = map(mapStr).toSimplified()
        assertEquals(map.width, 4)
        assertEquals(map.height, 2)
    }

    @Test
    fun test4x1simplified() {
        val mapStr = """
|w
w
w
w
        """.trim()
        val map = map(mapStr).toSimplified()
        assertEquals(map.width, 1)
        assertEquals(map.height, 1)
    }
}
