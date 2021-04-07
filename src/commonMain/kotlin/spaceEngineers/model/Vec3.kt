package spaceEngineers.model

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


data class Vec3(
    val x: Float,
    val y: Float,
    val z: Float
) {

    constructor(
        x: Double, y: Double, z: Double
    ) : this(x.toFloat(), y.toFloat(), z.toFloat())

    fun distanceTo(p: Vec3): Float {
        return sqrt(
            (x - p.x).pow(2.0f) +
                    (y - p.y).pow(2.0f) +
                    (z - p.z).pow(2.0f)
        )
    }

    fun dist(a: Vec3, b: Vec3): Float {
        return (a - b).length()
    }

    fun similar(other: Vec3, delta: Float = 0.1f): Boolean {
        return abs(x - other.x) < delta &&
                abs(y - other.y) < delta &&
                abs(z - other.z) < delta
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

        val ZERO by lazy {
            zero()
        }

        fun zero(): Vec3 {
            return Vec3(0f, 0f, 0f)
        }
    }
}