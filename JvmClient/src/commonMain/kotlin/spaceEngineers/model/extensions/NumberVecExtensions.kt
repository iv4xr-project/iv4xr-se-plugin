package spaceEngineers.model.extensions

import spaceEngineers.model.NumberVec
import spaceEngineers.model.Vec3

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
