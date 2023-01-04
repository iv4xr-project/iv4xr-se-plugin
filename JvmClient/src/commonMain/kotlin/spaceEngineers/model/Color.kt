package spaceEngineers.model

object Color {
    val RED = Vec3F(1, 0, 0)
    val GREEN = Vec3F(0, 1, 0)
    val BLUE = Vec3F(0, 0, 1)
    val PINE_TREE = fromByteRGB(42u, 47u, 35u) * 2f

    fun fromByteRGB(red: UByte, green: UByte, blue: UByte): Vec3F {
        return Vec3F(
            red.toDouble() / 255.0,
            green.toDouble() / 255.0,
            blue.toDouble() / 255.0,
        )
    }
}
