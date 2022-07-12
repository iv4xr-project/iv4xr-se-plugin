package spaceEngineers.model

import spaceEngineers.model.extensions.times
import testhelp.assertVecEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class RotationMatrixTest {


    private fun assertEquals(v1: Vec3F, v2: Vec3F, diff: Float = 0f) {
        assertVecEquals(v1, v2, absoluteTolerance = diff)
    }

    private fun assertEquals(m1: RotationMatrix, m2: RotationMatrix, diff: Float = 0f) {
        m1.values.forEachIndexed { index, fl ->
            assertEquals(
                fl,
                m2.values[index],
                diff,
                message = "error for index: $index $fl vs ${m2.values[index]}"
            )
        }
    }

    @Test
    fun matrix() {
        val matrix = RotationMatrix(
            floatArrayOf(
                1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f
            )
        )
        assertEquals(Vec3F(0f, 0f, -1f), matrix.forward)
        assertEquals(Vec3F(0f, 1f, 0f), matrix.up)
    }

    @Test
    fun matrix2() {
        val matrix = RotationMatrix(
            floatArrayOf(
                1f, 0f, 0f,
                0f, 0f, 1f,
                0f, -1f, 0f
            )
        )
        assertEquals(-Vec3F(0f, 1f, 0f), matrix.forward)
        assertEquals(Vec3F(0f, 0f, -1f), matrix.up)
        assertEquals(Vec3F(1f, 0f, 0f), matrix.right)
    }

    @Test
    fun matrix3() {
        val matrix = RotationMatrix(
            floatArrayOf(
                0.95163226f, 7.147936E-6f, 0.30723935f,
                -5.5080022E-6f, 1.0f, -6.2047493E-6f,
                -0.30723935f, 4.212364E-6f, 0.95163226f,
            )
        )
        assertEquals(0.95163226f, matrix[0, 0])
        assertEquals(-0.30723935f, matrix[0, 2])
        assertEquals(-Vec3F(x = 0.30723935f, y = -6.2047493E-6f, z = 0.95163226f), matrix.forward)
        assertEquals(Vec3F(x = 7.147936E-6f, y = 1.0f, z = 4.212364E-6f), matrix.up)
        assertEquals(Vec3F(0.95163226f, -5.5080022E-6f, -0.30723935f), matrix.right)


        assertEquals(
            matrix, RotationMatrix.fromForwardAndUp(matrix.forward, matrix.up), diff = 0.001f
        )
    }

    @Test
    fun vectors() {
        val matrix = RotationMatrix.IDENTITY
        assertEquals(matrix.forward, Vec3F.FORWARD)
        assertEquals(matrix.up, Vec3F.UP)
        assertEquals(matrix.right, Vec3F.RIGHT)

        assertEquals(matrix.forward, Vec3F(0, 0, -1))
        assertEquals(matrix.up, Vec3F(0, 1, 0))

        assertEquals(matrix.backward, Vec3F(0, 0, 1))
        assertEquals(matrix.down, Vec3F(0, -1, 0))
        assertEquals(matrix.left, Vec3F(-1, 0, 0))
        assertEquals(matrix.right, Vec3F(1, 0, 0))
    }

    @Test
    fun mul() {
        val matrix = RotationMatrix(
            1, 2, 3,
            4, 5, 6,
            7, 8, 9
        )
        val v = Vec3F(2, 1, 3)
        assertEquals(Vec3F(13, 31, 49), matrix * v)
    }
}
