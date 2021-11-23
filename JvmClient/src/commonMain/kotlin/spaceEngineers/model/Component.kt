package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Component(
    @SerialName("Definition")
    val definition: DataPhysicalItemDefinition,
    @SerialName("Count")
    val count: Int,
    @SerialName("DeconstructItem")
    val deconstructItem: DataPhysicalItemDefinition,
)
