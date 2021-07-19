package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MountPoint(
    @SerialName("Normal")
    val normal: Vec3,
    @SerialName("Start")
    val start: Vec3,
    @SerialName("End")
    val end: Vec3,
    @SerialName("Enabled")
    val enabled: Boolean = true,
    @SerialName("Default")
    val default: Boolean = false,
    @SerialName("PressurizedWhenOpen")
    val pressurizedWhenOpen: Boolean = false,
    @SerialName("ExclusionMask")
    val exclusionMask: Byte = 0,
    @SerialName("PropertiesMask")
    val propertiesMask: Byte = 0,
)
