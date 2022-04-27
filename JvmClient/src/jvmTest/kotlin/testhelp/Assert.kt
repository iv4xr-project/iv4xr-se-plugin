package testhelp


import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.Vec3F
import kotlin.test.assertEquals
import kotlin.test.assertTrue


const val DEFAULT_VECTOR_EQUALITY_TOLERANCE: Float = 0.01f
const val DEFAULT_VECTOR_DIRECTION_TOLERANCE: Float = 0.0001f
const val DEFAULT_SPEED_TOLERANCE: Float = 0.05f
const val DEFAULT_POSITION_TOLERANCE: Float = 0.1f
const val DEFAULT_ORIENTATION_TOLERANCE: Float = 0.1f

fun assertSameDirection(vec1: Vec3F, vec2: Vec3F, absoluteTolerance: Float = DEFAULT_VECTOR_DIRECTION_TOLERANCE) {
    assertLessThan((vec1.normalized() - vec2.normalized()).length(), absoluteTolerance)
}

fun assertLessThan(lower: Float, higher: Float, message: String? = null) {
    assertTrue(lower < higher, message ?: "$lower is not lower than $higher")
}

fun assertGreaterThan(higher: Float, lower: Float, message: String? = null) {
    assertTrue(higher > lower, message ?: "$higher is not greater than $lower")
}


fun assertVecEquals(v1: Vec3F, v2: Vec3F, absoluteTolerance: Float = DEFAULT_VECTOR_EQUALITY_TOLERANCE, message: String = "") {
    assertEquals(
        v1.x,
        v2.x,
        absoluteTolerance = absoluteTolerance,
        message = "$message Vectors not equal (x) ${v1.x} vs ${v2.x} ($v1 vs $v2)"
    )
    assertEquals(
        v1.y,
        v2.y,
        absoluteTolerance = absoluteTolerance,
        message = "$message Vectors not equal (y) ${v1.y} vs ${v2.y} ($v1 vs $v2)"
    )
    assertEquals(
        v1.z,
        v2.z,
        absoluteTolerance = absoluteTolerance,
        message = "$message Vectors not equal (z) ${v1.z} vs ${v2.z} ($v1 vs $v2)"
    )
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
