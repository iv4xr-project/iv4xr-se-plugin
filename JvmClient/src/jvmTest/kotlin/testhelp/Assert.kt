package testhelp


import spaceEngineers.model.Observation
import spaceEngineers.model.Vec3
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

fun checkMockObservation(obs: Observation?) {
    assertNotNull(obs)
    obs.let { observation ->
        assertEquals("Mock", observation.id)
        assertEquals(Vec3(4.0f, 2.0f, 0.0f), observation.position)
        observation.grids.first().let { grid ->
            assertEquals(1, grid.blocks.size)
            grid.blocks.first().let { block ->
                assertEquals("blk", block.id)
                assertEquals(10f, block.maxIntegrity)
                assertEquals(1f, block.buildIntegrity)
                assertEquals(5f, block.integrity)
                assertEquals("MockBlock", block.blockType)
                assertEquals(Vec3(5f, 5f, 5f), block.position)
            }
        }
    }
}

fun assertFloatEquals(expected: Float, result: Float, diff: Float = 0.1f, message: String? = null) {
    val calculatedDiff = abs(expected - result)
    assertTrue(calculatedDiff <= diff, message ?: "calculated diff is $calculatedDiff")
}

fun assertVecEquals(v1: Vec3, v2: Vec3, diff: Float = 0.01f, message: String = "") {
    assertFloatEquals(v1.x, v2.x, diff = diff, message = "$message Vectors not equal (x) ${v1.x} vs ${v2.x} ($v1 vs $v2)")
    assertFloatEquals(v1.y, v2.y, diff = diff, message = "$message Vectors not equal (y) ${v1.y} vs ${v2.y} ($v1 vs $v2)")
    assertFloatEquals(v1.z, v2.z, diff = diff, message = "$message Vectors not equal (z) ${v1.z} vs ${v2.z} ($v1 vs $v2)")
}
