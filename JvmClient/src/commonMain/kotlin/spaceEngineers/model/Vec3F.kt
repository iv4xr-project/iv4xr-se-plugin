package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import spaceEngineers.model.extensions.sum
import kotlin.math.abs
import kotlin.math.sqrt


@Serializable
data class Vec3F(
    @SerialName("X")
    override val x: Float = 0f,
    @SerialName("Y")
    override val y: Float = 0f,
    @SerialName("Z")
    override val z: Float = 0f,
) : NumberVec3<Float> {

    constructor(
        x: Double = 0.0, y: Double = 0.0, z: Double = 0.0
    ) : this(x.toFloat(), y.toFloat(), z.toFloat())

    constructor(
        x: Int = 0, y: Int = 0, z: Int = 0
    ) : this(x.toFloat(), y.toFloat(), z.toFloat())

    fun distanceTo(other: Vec3F): Float {
        return (this - other).length()
    }

    fun similar(other: Vec3F, delta: Float = 0.1f): Boolean {
        return abs(x - other.x) < delta &&
                abs(y - other.y) < delta &&
                abs(z - other.z) < delta
    }

    operator fun unaryMinus(): Vec3F {
        return times(-1f)
    }

    operator fun minus(other: Vec3F): Vec3F {
        return Vec3F(x - other.x, y - other.y, z - other.z)
    }

    operator fun plus(other: Vec3F): Vec3F {
        return Vec3F(x + other.x, y + other.y, z + other.z)
    }

    operator fun div(value: Float): Vec3F {
        return Vec3F(x / value, y / value, z / value)
    }

    operator fun times(b: Vec3F): Float {
        val a = this
        return a.x * b.x + a.y * b.y + a.z * b.z
    }

    operator fun times(scalar: Float): Vec3F {
        return Vec3F(x * scalar, y * scalar, z * scalar)
    }

    fun length(): Float {
        return sqrt(this * this)
    }

    fun normalized(): Vec3F {
        val s = length()
        if (s == 0f) throw ArithmeticException("Cannot normalize zero vector!")
        return this / s
    }

    companion object {

        fun directionFromString(value: String): Vec3F {
            return when (value) {
                "forward" -> FORWARD
                "backward" -> BACKWARD
                "left" -> LEFT
                "right" -> RIGHT
                "up" -> UP
                "down" -> DOWN
                else -> {
                    if (value.contains("-")) {
                        value.split("-").map(::directionFromString).sum().normalized()
                    } else {
                        error("No vector defined for value $value")
                    }
                }
            }
        }

        val UNIT_X = Vec3F(1, 0, 0)
        val UNIT_Y = Vec3F(0, 1, 0)
        val UNIT_Z = Vec3F(0, 0, 1)
        val MAX_VALUE = Vec3F(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        val MIN_VALUE = Vec3F(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        val UP = Vec3F(0, 1, 0)
        val DOWN = Vec3F(0, -1, 0)
        val RIGHT = Vec3F(1, 0, 0)
        val LEFT = Vec3F(-1, 0, 0)
        val FORWARD = Vec3F(0, 0, -1)
        val BACKWARD = Vec3F(0, 0, 1)

        val ONE = Vec3F(1, 1, 1)
        val ZERO = Vec3F(0f, 0f, 0f)

        val RED = Vec3F(1, 0, 0)
        val GREEN = Vec3F(0, 1, 0)
        val BLUE = Vec3F(0, 0, 1)
    }
}
