package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataPhysicalItemDefinition(
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,

    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("Mass")
    override val mass: Float,
    @SerialName("Volume")
    override val volume: Float,
    @SerialName("Health")
    override val health: Int,
) : PhysicalItemDefinition
