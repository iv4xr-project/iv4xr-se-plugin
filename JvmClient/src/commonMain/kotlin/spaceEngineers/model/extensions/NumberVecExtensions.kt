package spaceEngineers.model.extensions

import spaceEngineers.model.*
import spaceEngineers.movement.BasicDirection3d
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

fun Vec3F.normalizeAsMovement(characterMovementType: CharacterMovementType): Vec3F {
    return normalized() * characterMovementType.speed
}

fun Vec3F.normalizeAsWalk(): Vec3F {
    return normalizeAsMovement(CharacterMovementType.WALK)
}

fun Vec3F.normalizeAsRun(): Vec3F {
    return normalizeAsMovement(CharacterMovementType.RUN)
}

fun Vec3F.normalizeAsSprint(): Vec3F {
    return normalizeAsMovement(CharacterMovementType.SPRINT)
}

fun NumberVec3<Float>.crossProduct(b: NumberVec3<Float>): Vec3F {
    val a = this
    return Vec3F(
        a.y * b.z - a.z * b.y,
        a.z * b.x - a.x * b.z,
        a.x * b.y - a.y * b.x
    )
}

fun NumberVec3<Float>.toArray(): FloatArray {
    return floatArrayOf(x, y, z)
}

fun Vec3I.toFloat(): Vec3F {
    return Vec3F(x, y, z)
}

fun Vec3F.toInt(): Vec3I {
    return Vec3I(x, y, z)
}

fun Vec3F.roundToInt(): Vec3I {
    return Vec3I(x.roundToInt(), y.roundToInt(), z.roundToInt())
}

inline fun Iterable<Vec3F>.sum(): Vec3F {
    var sum = Vec3F.ZERO
    for (element in this) {
        sum += element
    }
    return sum
}

fun Vec3I.neighbourPositions(): Set<Vec3I> {
    return BasicDirection3d.excludingNone().map {
        this + it.vector.toInt()
    }.toSet()
}

fun stepSgn(value: Int): Int {
    return if (value < 0) {
        -1
    } else {
        1
    }
}

fun Vec3I.allBetween(end: Vec3I): Sequence<Vec3I> = sequence {
    (x..end.x step stepSgn((end.x - x))).forEach { newX ->
        (y..end.y step stepSgn((end.y - y))).forEach { newY ->
            (z..end.z step stepSgn((end.z - z))).forEach { newZ ->
                yield(Vec3I(newX, newY, newZ))
            }
        }
    }
}

fun Vec3F.resizeTo(size: Float): Vec3F {
    return normalized() * size
}

fun Vec3I.lineBetween(end: Vec3I, extraWidth: Float = 0.1f): Sequence<Vec3I> = sequence {

    val diff = end - this@lineBetween

    val steps = diff.length() * 1000

    val step = diff / steps

    var current = this@lineBetween.toFloat()

    yield(current.roundToInt())
    repeat(steps.toInt()) {
        current += step
        yield(current.roundToInt())
        current.perpendicularVectors(10).forEach {
            yield((current + it.resizeTo(extraWidth)).roundToInt())
        }
    }
    yield(end)
}.distinct()

fun Vec3F.rotate(axis: Vec3F, angleRad: Float): Vec3F {
    return axis.rotationMatrix(angleRad) * this
}

fun Vec3F.perpendicularVectors(numVectors: Int): List<Vec3F> {
    val unitVector = normalized()
    var randomVector = Vec3F.UP
    if (abs((unitVector - randomVector).length()) < 0.1f) {
        randomVector = Vec3F.RIGHT
    }
    val perpendicular = this.crossProduct(randomVector)
    return perpendicular.perpendicularVectors(this, numVectors)
}

fun Vec3F.perpendicularVectors(axis: Vec3F, numVectors: Int, offsetRad: Int = 0): List<Vec3F> {
    if (numVectors < 1) {
        throw IllegalArgumentException("Number of vectors must be at least 1")
    }
    val normalizedAxis = axis.normalized()
    val x = this.normalized()
    val angle = Math.toRadians(360.0 / numVectors.toFloat()).toFloat()
    return List(numVectors) {
        x.rotate(axis = normalizedAxis, angleRad = offsetRad + angle * it)
    }
}

fun Vec3F.rotationMatrix(angle: Float): RotationMatrix {
    val normalizedAxis = this.normalized()
    val c = cos(angle)
    val s = sin(angle)
    val t = 1 - c
    return RotationMatrix(
        floatArrayOf(
            t * normalizedAxis.x * normalizedAxis.x + c,
            t * normalizedAxis.x * normalizedAxis.y - s * normalizedAxis.z,
            t * normalizedAxis.x * normalizedAxis.z + s * normalizedAxis.y,
            t * normalizedAxis.x * normalizedAxis.y + s * normalizedAxis.z,
            t * normalizedAxis.y * normalizedAxis.y + c,
            t * normalizedAxis.y * normalizedAxis.z - s * normalizedAxis.x,
            t * normalizedAxis.x * normalizedAxis.z - s * normalizedAxis.y,
            t * normalizedAxis.y * normalizedAxis.z + s * normalizedAxis.x,
            t * normalizedAxis.z * normalizedAxis.z + c
        )
    )
}
