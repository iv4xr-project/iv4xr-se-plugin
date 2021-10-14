package spaceEngineers.model

// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.


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
    @SerialName("Mass")
    override val mass: Float,
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
) : AirtightDoorGenericDefinition

@Serializable
data class DataLCDPanelsBlockDefinition(
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
    @SerialName("Mass")
    override val mass: Float,
    @SerialName("MountPoints")
    override val mountPoints: List<MountPoint>,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,



    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
) : LCDPanelsBlockDefinition

@Serializable
data class DataPowerProducerDefinition(
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
    @SerialName("Mass")
    override val mass: Float,
    @SerialName("MountPoints")
    override val mountPoints: List<MountPoint>,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,



    @SerialName("MaxPowerOutput")
    override val maxPowerOutput: Float,
) : PowerProducerDefinition

@Serializable
data class DataGravityGeneratorDefinition(
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
    @SerialName("Mass")
    override val mass: Float,
    @SerialName("MountPoints")
    override val mountPoints: List<MountPoint>,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,

    @SerialName("MinGravityAcceleration")
    override val minGravityAcceleration: Float,
    @SerialName("MaxGravityAcceleration")
    override val maxGravityAcceleration: Float,

    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
) : GravityGeneratorDefinition

@Serializable
data class DataGravityGeneratorBaseDefinition(
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
    @SerialName("Mass")
    override val mass: Float,
    @SerialName("MountPoints")
    override val mountPoints: List<MountPoint>,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,



    @SerialName("MinGravityAcceleration")
    override val minGravityAcceleration: Float,
    @SerialName("MaxGravityAcceleration")
    override val maxGravityAcceleration: Float,
) : GravityGeneratorBaseDefinition

