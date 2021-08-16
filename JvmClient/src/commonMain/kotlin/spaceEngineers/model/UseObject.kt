package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UseObject(
    @SerialName("Name")
    val name: String,
    @SerialName("SupportedActions")
    val SupportedActions: Int,
    @SerialName("PrimaryAction")
    val primaryAction: Int,
    @SerialName("SecondaryAction")
    val secondaryAction: Int,
    @SerialName("ContinuousUsage")
    val continuousUsage: Boolean,
)
