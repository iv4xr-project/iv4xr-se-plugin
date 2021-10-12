package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataAirtightDoorGenericDefinition(
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("BuildProgressModels")
    override val buildProgressModels: List<BuildProgressModel>,
    @SerialName("Type")
    override val type: String,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("CubeSize")
    override val cubeSize: CubeSize,
    @SerialName("MountPoints")
    override val mountPoints: List<MountPoint>,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,

    @SerialName("PowerConsumptionIdle")
    override val powerConsumptionIdle: Float,
    @SerialName("PowerConsumptionMoving")
    override val powerConsumptionMoving: Float,
    @SerialName("OpeningSpeed")
    override val openingSpeed: Float,
): AirtightDoorGenericDefinition
