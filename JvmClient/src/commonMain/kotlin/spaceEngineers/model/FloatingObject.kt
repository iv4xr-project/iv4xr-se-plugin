package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FloatingObject(
    @SerialName("EntityId")
    val entityId: Long,
    @SerialName("Amount")
    val amount: Float,
    @SerialName("DisplayName")
    val displayName: String,
    @SerialName("ItemDefinition")
    val itemDefinition: PhysicalItemDefinition,
)
