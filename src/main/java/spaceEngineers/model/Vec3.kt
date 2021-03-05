package spaceEngineers.model


data class Vec3(
    val x: Float,
    val y: Float,
    val z: Float
) {
    companion object {
        fun zero(): Vec3 {
            return Vec3(0f, 0f, 0f)
        }
    }
}