package spaceEngineers.model

import kotlin.math.abs
import kotlin.math.sqrt


data class Vec3(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
) {

    constructor(
        x: Double = 0.0, y: Double = 0.0, z: Double = 0.0
    ) : this(x.toFloat(), y.toFloat(), z.toFloat())

    constructor(
        x: Int = 0, y: Int = 0, z: Int = 0
    ) : this(x.toFloat(), y.toFloat(), z.toFloat())

    fun distanceTo(other: Vec3): Float {
        return (this - other).length()
    }

    fun similar(other: Vec3, delta: Float = 0.1f): Boolean {
        return abs(x - other.x) < delta &&
                abs(y - other.y) < delta &&
                abs(z - other.z) < delta
    }

    operator fun unaryMinus(): Vec3 {
        return times(-1f)
    }

    operator fun minus(other: Vec3): Vec3 {
        return Vec3(x - other.x, y - other.y, z - other.z)
    }

    operator fun plus(other: Vec3): Vec3 {
        return Vec3(x + other.x, y + other.y, z + other.z)
    }

    operator fun div(value: Float): Vec3 {
        return Vec3(x / value, y / value, z / value)
    }

    operator fun times(b: Vec3): Float {
        val a = this
        return a.x * b.x + a.y * b.y + a.z * b.z
    }

    operator fun times(scalar: Float): Vec3 {
        return Vec3(x * scalar, y * scalar, z * scalar)
    }

    fun length(): Float {
        return sqrt(this * this)
    }

    fun normalized(): Vec3 {
        val s = length()
        if (s == 0f) throw ArithmeticException()
        return this / s
    }


    val size: Float by lazy {
        distanceTo(ZERO)
    }

    companion object {
        val ROTATE_LEFT = Vec3(0f, -1f, 0f)
        val ROTATE_RIGHT = Vec3(0f, 1f, 0f)
        val ROTATE_UP = Vec3(-1f, 0f, 0f)
        val ROTATE_DOWN = Vec3(1f, 0f, 0f)
        val ROTATE_UP_LEFT = Vec3(-1f, -1f, 0f).normalized()
        val ROTATE_UP_RIGHT = Vec3(-1f, 1f, 0f).normalized()


        val ZERO by lazy {
            zero()
        }

        fun zero(): Vec3 {
            return Vec3(0f, 0f, 0f)
        }
    }
}