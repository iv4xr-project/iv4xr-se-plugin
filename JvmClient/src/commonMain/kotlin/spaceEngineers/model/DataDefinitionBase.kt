package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataDefinitionBase(
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,
): DefinitionBase
