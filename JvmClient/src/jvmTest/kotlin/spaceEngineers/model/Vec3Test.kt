package spaceEngineers.model

import spaceEngineers.model.Vec3.Companion.ZERO
import testhelp.assertVecEquals
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class Vec3Test {

    private val x1 = Vec3(x = 1)
    private val x2 = Vec3(x = 2)
    private val xn1 = Vec3(x = -1)
    private val xy1 = Vec3(x = 1, y = 1)
    private val xyz1 = Vec3(x = 1, y = 1, z = 1)

    private fun assertEquals(v1: Vec3, v2: Vec3, diff: Float = 0f) {
        assertVecEquals(v1, v2, diff = diff)
    }

    @Test
    fun plus() {
        assertEquals(ZERO, x1 + xn1)
        assertEquals(x1, x1 + ZERO)
        assertEquals(xn1, xn1 + ZERO)
    }

    @Test
    fun minus() {
        assertEquals(ZERO, x1 - x1)
        assertEquals(ZERO, xn1 - xn1)
        assertEquals(x1, x1 - ZERO)
        assertEquals(xn1, xn1 - ZERO)
        assertEquals(x1, ZERO - xn1)
        assertEquals(xn1, ZERO - x1)
    }

    @Test
    fun unaryMinus() {
        assertEquals(ZERO, -ZERO)
        assertEquals(x1, -xn1)
        assertEquals(xn1, -x1)
    }

    @Test
    fun distanceTo() {
        assertEquals(ZERO.distanceTo(ZERO), 0f)
        assertEquals(ZERO.distanceTo(x1), 1f)
        assertEquals(ZERO.distanceTo(xn1), 1f)
        assertEquals(x1.distanceTo(xn1), 2f)
        assertEquals(xn1.distanceTo(x1), 2f)
        assertEquals(ZERO.distanceTo(xy1), sqrt(2.0).toFloat())
        assertEquals(ZERO.distanceTo(xyz1), sqrt(3.0).toFloat())
        assertEquals(ZERO.distanceTo(xyz1), xyz1.length())
    }

    @Test
    fun timesScalar() {
        assertEquals(x2, x1 * 2f)
    }


}