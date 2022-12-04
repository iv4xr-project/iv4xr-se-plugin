package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.sqrt

@Serializable
data class Vec2F(
    @SerialName("X")
    override val x: Float = 0f,
    @SerialName("Y")
    override val y: Float = 0f,
) : NumberVec2<Float> {

    fun distanceTo(other: Vec2F): Float {
        return (this - other).length()
    }

    operator fun minus(other: Vec2F): Vec2F {
        return Vec2F(x - other.x, y - other.y)
    }

    operator fun times(b: Vec2F): Float {
        val a = this
        return a.x * b.x + a.y * b.y
    }

    operator fun times(scalar: Float): Vec2F {
        return Vec2F(x * scalar, y * scalar)
    }

    operator fun div(value: Float): Vec2F {
        return Vec2F(x / value, y / value)
    }

    fun length(): Float {
        return sqrt(this * this)
    }

    fun normalized(): Vec2F {
        val s = length()
        if (s == 0f) throw ArithmeticException()
        return this / s
    }

    companion object {
        val ROTATE_LEFT = Vec2F(0f, -1f)
        val ROTATE_RIGHT = Vec2F(0f, 1f)
        val ROTATE_UP = Vec2F(-1f, 0f)
        val ROTATE_DOWN = Vec2F(1f, 0f)
        val ROTATE_UP_LEFT = Vec2F(-1f, -1f).normalized()
        val ROTATE_UP_RIGHT = Vec2F(-1f, 1f).normalized()

        val UNIT_X = Vec2F(1f, 0f)
        val UNIT_Y = Vec2F(0f, 1f)
        val MAX_VALUE = Vec2F(Float.MAX_VALUE, Float.MAX_VALUE)
        val MIN_VALUE = Vec2F(Float.MIN_VALUE, Float.MIN_VALUE)
        val ONE = Vec2F(1f, 1f)
        val ZERO = Vec2F(0f, 0f)
    }
}
