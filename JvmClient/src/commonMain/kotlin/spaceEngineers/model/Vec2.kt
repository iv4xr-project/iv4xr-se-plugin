package spaceEngineers.model

import kotlin.math.sqrt


interface NumberVec2<out T : Number> {
    val x: T
    val y: T
}


data class Vec2(
    override val x: Float = 0f,
    override val y: Float = 0f,
) : NumberVec2<Float> {

    fun distanceTo(other: Vec2): Float {
        return (this - other).length()
    }

    operator fun minus(other: Vec2): Vec2 {
        return Vec2(x - other.x, y - other.y)
    }

    operator fun times(b: Vec2): Float {
        val a = this
        return a.x * b.x + a.y * b.y
    }

    operator fun times(scalar: Float): Vec2 {
        return Vec2(x * scalar, y * scalar)
    }

    operator fun div(value: Float): Vec2 {
        return Vec2(x / value, y / value)
    }

    fun length(): Float {
        return sqrt(this * this)
    }

    fun normalized(): Vec2 {
        val s = length()
        if (s == 0f) throw ArithmeticException()
        return this / s
    }

    val size: Float
        get() = distanceTo(ZERO)

    companion object {
        val ROTATE_LEFT = Vec2(0f, -1f)
        val ROTATE_RIGHT = Vec2(0f, 1f)
        val ROTATE_UP = Vec2(-1f, 0f)
        val ROTATE_DOWN = Vec2(1f, 0f)
        val ROTATE_UP_LEFT = Vec2(-1f, -1f).normalized()
        val ROTATE_UP_RIGHT = Vec2(-1f, 1f).normalized()

        val UNIT_X = Vec2(1f, 0f)
        val UNIT_Y = Vec2(0f, 1f)
        val MAX_VALUE = Vec2(Float.MAX_VALUE, Float.MAX_VALUE)
        val MIN_VALUE = Vec2(Float.MIN_VALUE, Float.MIN_VALUE)
        val ONE = Vec2(1f, 1f)
        val ZERO = Vec2(0f, 0f)
    }
}
