package spaceEngineers.model

import kotlin.math.pow
import kotlin.math.sqrt


data class Vec3(
    val x: Float,
    val y: Float,
    val z: Float
) {

    fun distanceTo(p: Vec3): Float {
        return sqrt(
            (x - p.x).pow(2.0f) +
                    (y - p.y).pow(2.0f) +
                    (z - p.z).pow(2.0f)
        )
    }

    val size: Float by lazy {
        distanceTo(ZERO)
    }

    companion object {
        val ZERO by lazy {
            zero()
        }

        fun zero(): Vec3 {
            return Vec3(0f, 0f, 0f)
        }
    }
}