package spaceEngineers.model.extensions

import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.NumberVec
import spaceEngineers.model.Vec3


fun Vec3.normalizeAsMovement(characterMovementType: CharacterMovementType): Vec3 {
    return normalized() * characterMovementType.speed
}

fun Vec3.normalizeAsWalk(): Vec3 {
    return normalizeAsMovement(CharacterMovementType.WALK)
}

fun Vec3.normalizeAsRun(): Vec3 {
    return normalizeAsMovement(CharacterMovementType.RUN)
}

fun Vec3.normalizeAsSprint(): Vec3 {
    return normalizeAsMovement(CharacterMovementType.SPRINT)
}

fun NumberVec<Float>.crossProduct(b: NumberVec<Float>): Vec3 {
    val a = this
    return Vec3(
        a.y * b.z - a.z * b.y,
        a.z * b.x - a.x * b.z,
        a.x * b.y - a.y * b.x
    )
}

fun NumberVec<Float>.toArray(): FloatArray {
    return floatArrayOf(x, y, z)
}
