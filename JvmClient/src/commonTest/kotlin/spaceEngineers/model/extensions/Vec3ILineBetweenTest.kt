package spaceEngineers.model.extensions

import spaceEngineers.model.Vec3I
import kotlin.test.Test
import kotlin.test.assertEquals

class Vec3ILineBetweenTest {
    @Test
    fun testLineBetweenPositiveCoordinates() {
        val start = Vec3I(1, 1, 1)
        val end = Vec3I(3, 3, 3)
        val expected = listOf(
            Vec3I(x = 1, y = 1, z = 1),
            Vec3I(x = 1, y = 1, z = 2),
            Vec3I(x = 2, y = 1, z = 1),
            Vec3I(x = 1, y = 2, z = 1),
            Vec3I(x = 2, y = 2, z = 1),
            Vec3I(x = 1, y = 2, z = 2),
            Vec3I(x = 2, y = 1, z = 2),
            Vec3I(x = 2, y = 2, z = 2),
            Vec3I(x = 2, y = 2, z = 3),
            Vec3I(x = 3, y = 2, z = 2),
            Vec3I(x = 2, y = 3, z = 2),
            Vec3I(x = 3, y = 3, z = 2),
            Vec3I(x = 2, y = 3, z = 3),
            Vec3I(x = 3, y = 2, z = 3),
            Vec3I(3, 3, 3)
        )
        val actual = start.lineBetween(end).toList()
        assertEquals(expected, actual)
    }

    @Test
    fun testLineBetweenLength1() {
        val start = Vec3I(1, 1, 1)
        val end = Vec3I(1, 1, 1)
        val expected = listOf(Vec3I(1, 1, 1))
        val actual = start.lineBetween(end).toList()
        assertEquals(expected, actual)
    }

    @Test
    fun testLineBetweenNonDiagonal() {
        val start = Vec3I(1, 1, 1)
        val end = Vec3I(3, 1, 1)
        val expected = listOf(Vec3I(1, 1, 1), Vec3I(2, 1, 1), Vec3I(3, 1, 1))
        val actual = start.lineBetween(end).toList()
        assertEquals(expected, actual)
    }
}
