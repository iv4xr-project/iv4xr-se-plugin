package spaceEngineers.model.extensions

import spaceEngineers.model.Vec3F
import testhelp.assertVecEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class PerpendicularVectorTest {

    @Test
    fun testForwardUp4() {
        val expected = listOf(Vec3F.FORWARD, Vec3F.LEFT, Vec3F.BACKWARD, Vec3F.RIGHT)
        Vec3F.FORWARD.perpendicularVectors(Vec3F.UP, 4).forEachIndexed { index, v ->
            assertVecEquals(expected[index], v)
            assertEquals(0f, v.y)
        }
    }

    @Test
    fun downRight4() {
        val expected = listOf(Vec3F.DOWN, Vec3F.FORWARD, Vec3F.UP, Vec3F.BACKWARD)
        Vec3F.DOWN.perpendicularVectors(Vec3F.RIGHT, 4).forEachIndexed { index, v ->
            assertEquals(0f, v.x)
            assertVecEquals(expected[index], v)
        }
    }
}
