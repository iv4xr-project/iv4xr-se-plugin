package spaceEngineers.model.extensions

import spaceEngineers.model.RotationMatrix
import spaceEngineers.model.Vec3

operator fun RotationMatrix.times(vec: Vec3): Vec3 {
    return Vec3(
        this[0, 0] * vec.x + this[1, 0] * vec.y + this[2, 0] * vec.z,
        this[0, 1] * vec.x + this[1, 1] * vec.y + this[2, 1] * vec.z,
        this[0, 2] * vec.x + this[1, 2] * vec.y + this[2, 2] * vec.z,
    )
}

operator fun RotationMatrix.unaryMinus(): RotationMatrix {
    return RotationMatrix(
        values = this.values.map { -it }.toFloatArray()
    )
}
