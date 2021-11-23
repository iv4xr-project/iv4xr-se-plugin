package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MountPoint(
    @SerialName("Normal")
    val normal: Vec3F,
    @SerialName("Start")
    val start: Vec3F,
    @SerialName("End")
    val end: Vec3F,
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
