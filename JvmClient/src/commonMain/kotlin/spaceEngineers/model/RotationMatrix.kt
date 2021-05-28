package spaceEngineers.model

import spaceEngineers.model.extensions.crossProduct
import spaceEngineers.model.extensions.toArray


inline class RotationMatrix(
    val values: FloatArray = FloatArray(9)
) {

    constructor(vararg x: Int) : this(x.map { it.toFloat() }.toFloatArray())

    init {
        check(values.size == 9) {
            "matrix has to be array of 9, not ${values.size}"
        }
    }

    operator fun get(x: Int, y: Int): Float {
        return values[x + y * 3]
    }

    val forward: Vec3
        get() = -Vec3(get(2, 0), get(2, 1), get(2, 2))

    val backward: Vec3
        get() = -forward

    val up: Vec3
        get() = Vec3(get(1, 0), get(1, 1), get(1, 2))

    val down: Vec3
        get() = -up

    val left: Vec3
        get() = -right

    val right: Vec3
        get() = Vec3(get(0, 0), get(0, 1), get(0, 2))

    override fun toString(): String {
        return values.toList().chunked(3).joinToString("\n") { row ->
            row.joinToString(",", postfix = "]", prefix = "[") { it.toString() }
        }
    }

    companion object {
        fun fromForwardAndUp(forward: Vec3, up: Vec3): RotationMatrix {
            check(forward != up) {
                "orientations cannot be same"
            }
            val z = forward.normalized()
            val y = up.normalized()
            val x = z.crossProduct(y).normalized()
            return fromColumns(x, y, -z)
        }

        fun fromColumns(col1: Vec3, col2: Vec3, col3: Vec3): RotationMatrix {
            return RotationMatrix(
                floatArrayOf(
                    col1.x, col2.x, col3.x,
                    col1.y, col2.y, col3.y,
                    col1.z, col2.z, col3.z,
                )
            )
        }

        fun fromRows(row1: Vec3, row2: Vec3, row3: Vec3): RotationMatrix {
            return RotationMatrix(
                row1.toArray() + row2.toArray() + row3.toArray()
            )
        }

        val IDENTITY = RotationMatrix(
            values = floatArrayOf(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
        )
    }
}
