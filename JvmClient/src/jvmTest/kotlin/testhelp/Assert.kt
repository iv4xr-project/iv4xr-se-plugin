package testhelp


import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.Observation
import spaceEngineers.model.Vec3F
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

fun checkMockObservation(obs: Observation?) {
    assertNotNull(obs)
    obs.let { observation ->
        assertEquals("Mock", observation.character.id)
        assertEquals(Vec3F(4.0f, 2.0f, 0.0f), observation.character.position)
        observation.grids.first().let { grid ->
            assertEquals(1, grid.blocks.size)
            grid.blocks.first().let { block ->
                assertEquals("blk", block.id)
                assertEquals(10f, block.maxIntegrity)
                assertEquals(1f, block.buildIntegrity)
                assertEquals(5f, block.integrity)
                assertEquals("MockBlock", block.definitionId.type)
                assertEquals(Vec3F(5f, 5f, 5f), block.position)
            }
        }
    }
}

fun assertFloatEquals(expected: Float, result: Float, diff: Float = 0.1f, message: String? = null) {
    val calculatedDiff = abs(expected - result)
    assertTrue(calculatedDiff <= diff, message ?: "calculated diff is $calculatedDiff")
}

fun assertVecEquals(v1: Vec3F, v2: Vec3F, diff: Float = 0.01f, message: String = "") {
    assertFloatEquals(v1.x, v2.x, diff = diff, message = "$message Vectors not equal (x) ${v1.x} vs ${v2.x} ($v1 vs $v2)")
    assertFloatEquals(v1.y, v2.y, diff = diff, message = "$message Vectors not equal (y) ${v1.y} vs ${v2.y} ($v1 vs $v2)")
    assertFloatEquals(v1.z, v2.z, diff = diff, message = "$message Vectors not equal (z) ${v1.z} vs ${v2.z} ($v1 vs $v2)")
}

fun assertCharacterObservationEquals(co1: CharacterObservation, co2: CharacterObservation) {
    assertEquals(co1.id, co2.id)
    assertEquals(co1.position, co2.position)
    assertEquals(co1.orientationForward, co2.orientationForward)
    assertEquals(co1.orientationUp, co2.orientationUp)
    assertEquals(co1.velocity, co2.velocity)
    assertEquals(co1.extent, co2.extent)
    assertEquals(co1.jetpackRunning, co2.jetpackRunning)
    assertEquals(co1.helmetEnabled, co2.helmetEnabled)
    assertEquals(co1.health, co2.health)
    assertEquals(co1.oxygen, co2.oxygen)
    assertEquals(co1.hydrogen, co2.hydrogen)
    assertEquals(co1.suitEnergy, co2.suitEnergy)
    //assertEquals(co1.camera, co2.camera)
    assertEquals(co1.headLocalXAngle, co2.headLocalXAngle)
    assertEquals(co1.headLocalYAngle, co2.headLocalYAngle)
    assertEquals(co1.targetBlock, co2.targetBlock)
    assertEquals(co1.targetUseObject, co2.targetUseObject)
    assertEquals(co1.movement, co2.movement)
    assertEquals(co1.inventory, co2.inventory)
    assertEquals(co1.bootsState, co2.bootsState)
}
