package spaceEngineers.model

import kotlin.math.abs
import kotlin.math.sqrt


interface NumberVec<out T : Number> {
    val x: T
    val y: T
    val z: T
}

data class Vec3(
    override val x: Float = 0f,
    override val y: Float = 0f,
    override val z: Float = 0f,
) : NumberVec<Float> {

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


    val size: Float
        get() = distanceTo(ZERO)

    companion object {
        val UNIT_X = Vec3(1, 0, 0)
        val UNIT_Y = Vec3(0, 1, 0)
        val UNIT_Z = Vec3(0, 0, 1)
        val MAX_VALUE = Vec3(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        val MIN_VALUE = Vec3(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        val UP = Vec3(0, 1, 0)
        val DOWN = Vec3(0, -1, 0)
        val RIGHT = Vec3(1, 0, 0)
        val LEFT = Vec3(-1, 0, 0)
        val FORWARD = Vec3(0, 0, -1)
        val BACKWARD = Vec3(0, 0, 1)

        val ONE = Vec3(1, 1, 1)
        val ZERO = Vec3(0f, 0f, 0f)
    }
}
