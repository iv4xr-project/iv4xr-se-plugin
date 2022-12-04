package spaceEngineers.util.generator.map

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class SimplifiedArrayTest {

    fun parseArray(map: String): Array<Array<Char>> {
        return map.lines().map {
            it.map { it }.toTypedArray()
        }.toTypedArray()
    }

    fun arrayToString(array: Array<Array<Char>>): String {
        return (0 until array.height).joinToString("\n") { y ->
            (0 until array.width).joinToString("") { x ->
                array[x][y].toString()
            }
        }
    }

    fun assertMapsEqual(map1: String, map2: String) {
        val m1 = parseArray(map1).simplifiedArray()
        val m2 = parseArray(map2)
        assertEquals(m1.width, m2.width, message = arrayToString(m1) + "\nvs\n" + arrayToString(m2))
        assertEquals(m1.height, m2.height, message = arrayToString(m1) + "\nvs\n" + arrayToString(m2))
        m1.indices.forEach {
            assertContentEquals(m1[it], m2[it], message = arrayToString(m1) + "\nvs\n" + arrayToString(m2))
        }
    }

    @Test
    fun test3x3vs1x1() {
        assertMapsEqual(
            """
                xxx
                xxx
                xxx
            """.trimIndent().trim(),
            "x"
        )
    }

    @Test
    fun test2x1vs1x1() {
        assertMapsEqual(
            """
                xx
            """.trimIndent().trim(),
            "x"
        )
    }

    @Test
    fun test1x2vs1x1() {
        assertMapsEqual(
            """
                x
                x
            """.trimIndent().trim(),
            "x"
        )
    }

    @Test
    fun test1x5vs1x3() {
        assertMapsEqual(
            """
                o
                x
                x
                x
                o
            """.trimIndent().trim(),
            """
                o
                x
                o
            """.trimIndent().trim()
        )
    }

    @Test
    fun test5x1vs3x1() {
        assertMapsEqual(
            """
                oxxxo
            """.trimIndent().trim(),
            """
                oxo
            """.trimIndent().trim()
        )
    }

    @Test
    fun test5x5vs3x3() {
        assertMapsEqual(
            """
                oxxxo
                xxxxx
                xxxxx
                xxxxx
                oxxxo
            """.trimIndent().trim(),
            """
                oxo
                xxx
                oxo
            """.trimIndent().trim()
        )
    }
}
