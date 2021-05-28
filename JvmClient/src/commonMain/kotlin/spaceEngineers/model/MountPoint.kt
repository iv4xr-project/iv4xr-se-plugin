package spaceEngineers.model

data class MountPoint(
    val normal: Vec3,
    val start: Vec3,
    val end: Vec3,
    val enabled: Boolean = true,
    val default: Boolean = false,
    val pressurizedWhenOpen: Boolean = false,
    val exclusionMask: Byte = 0,
    val propertiesMask: Byte = 0,
)
