package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.sqrt


@Serializable
data class PlainVec3I(
    @SerialName("X")
    override val x: Int = 0,
    @SerialName("Y")
    override val y: Int = 0,
    @SerialName("Z")
    override val z: Int = 0,
) : NumberVec3<Int> {

    constructor(
        x: Double = 0.0, y: Double = 0.0, z: Double = 0.0
    ) : this(x.toInt(), y.toInt(), z.toInt())

    constructor(
        x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f
    ) : this(x.toInt(), y.toInt(), z.toInt())


    fun distanceTo(other: NumberVec3<Int>): Float {
        return (this - other).length()
    }

    operator fun unaryMinus(): PlainVec3I {
        return PlainVec3I(-x, -y, -z)
    }

    operator fun minus(other: NumberVec3<Int>): PlainVec3I {
        return PlainVec3I(x - other.x, y - other.y, z - other.z)
    }

    operator fun plus(other: NumberVec3<Int>): PlainVec3I {
        return PlainVec3I(x + other.x, y + other.y, z + other.z)
    }

    operator fun div(value: Float): PlainVec3I {
        return PlainVec3I(x / value, y / value, z / value)
    }

    operator fun times(b: NumberVec3<out Number>): Float {
        val a = this
        return (a.x * b.x.toFloat() + a.y * b.y.toFloat() + a.z * b.z.toFloat()).toFloat()
    }

    operator fun times(scalar: Float): Vec3 {
        return Vec3(x * scalar, y * scalar, z * scalar)
    }

    fun length(): Float {
        return sqrt(this * this)
    }

    fun normalized(): PlainVec3I {
        val s = length()
        if (s == 0f) throw ArithmeticException()
        return this / s
    }

    companion object {
        val UNIT_X = PlainVec3I(1, 0, 0)
        val UNIT_Y = PlainVec3I(0, 1, 0)
        val UNIT_Z = PlainVec3I(0, 0, 1)
        val MAX_VALUE = PlainVec3I(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        val MIN_VALUE = PlainVec3I(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        val UP = PlainVec3I(0, 1, 0)
        val DOWN = PlainVec3I(0, -1, 0)
        val RIGHT = PlainVec3I(1, 0, 0)
        val LEFT = PlainVec3I(-1, 0, 0)
        val FORWARD = PlainVec3I(0, 0, -1)
        val BACKWARD = PlainVec3I(0, 0, 1)

        val ONE = PlainVec3I(1, 1, 1)
        val ZERO = PlainVec3I(0f, 0f, 0f)
    }
}
