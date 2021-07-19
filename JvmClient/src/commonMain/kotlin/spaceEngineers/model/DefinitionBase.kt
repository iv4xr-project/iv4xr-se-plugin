package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DefinitionBase(
    @SerialName("Id")
    val id: String,
    @SerialName("BlockType")
    val blockType: String,
    @SerialName("Public")
    val public: Boolean,
    @SerialName("AvailableInSurvival")
    val availableInSurvival: Boolean,
    @SerialName("Enabled")
    val enabled: Boolean,
)
