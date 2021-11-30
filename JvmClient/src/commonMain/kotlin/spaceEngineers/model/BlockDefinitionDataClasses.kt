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
    @SerialName("Components")
    override val components: List<Component>,



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
    @SerialName("Components")
    override val components: List<Component>,



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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MaxPowerOutput")
    override val maxPowerOutput: Float,
) : PowerProducerDefinition

@Serializable
data class DataAdvancedDoorDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("PowerConsumptionIdle")
    override val powerConsumptionIdle: Float,
    @SerialName("PowerConsumptionMoving")
    override val powerConsumptionMoving: Float,
) : AdvancedDoorDefinition

@Serializable
data class DataAirVentDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("StandbyPowerConsumption")
    override val standbyPowerConsumption: Float,
    @SerialName("OperationalPowerConsumption")
    override val operationalPowerConsumption: Float,
    @SerialName("VentilationCapacityPerSecond")
    override val ventilationCapacityPerSecond: Float,
) : AirVentDefinition

@Serializable
data class DataProductionBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("InventoryMaxVolume")
    override val inventoryMaxVolume: Float,
    @SerialName("StandbyPowerConsumption")
    override val standbyPowerConsumption: Float,
    @SerialName("OperationalPowerConsumption")
    override val operationalPowerConsumption: Float,
) : ProductionBlockDefinition

@Serializable
data class DataAssemblerDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("InventoryMaxVolume")
    override val inventoryMaxVolume: Float,
    @SerialName("StandbyPowerConsumption")
    override val standbyPowerConsumption: Float,
    @SerialName("OperationalPowerConsumption")
    override val operationalPowerConsumption: Float,

    @SerialName("AssemblySpeed")
    override val assemblySpeed: Float,
) : AssemblerDefinition

@Serializable
data class DataBatteryBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("MaxPowerOutput")
    override val maxPowerOutput: Float,

    @SerialName("MaxStoredPower")
    override val maxStoredPower: Float,
    @SerialName("InitialStoredPowerRatio")
    override val initialStoredPowerRatio: Float,
    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
    @SerialName("AdaptibleInput")
    override val adaptibleInput: Boolean,
) : BatteryBlockDefinition

@Serializable
data class DataBeaconDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MaxBroadcastRadius")
    override val maxBroadcastRadius: Float,
    @SerialName("MaxBroadcastPowerDrainkW")
    override val maxBroadcastPowerDrainkW: Float,
) : BeaconDefinition

@Serializable
data class DataButtonPanelDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("ButtonCount")
    override val buttonCount: Int,
) : ButtonPanelDefinition

@Serializable
data class DataCameraBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
    @SerialName("RequiredChargingInput")
    override val requiredChargingInput: Float,
    @SerialName("MinFov")
    override val minFov: Float,
    @SerialName("MaxFov")
    override val maxFov: Float,
    @SerialName("RaycastConeLimit")
    override val raycastConeLimit: Float,
    @SerialName("RaycastDistanceLimit")
    override val raycastDistanceLimit: Double,
    @SerialName("RaycastTimeMultiplier")
    override val raycastTimeMultiplier: Float,
) : CameraBlockDefinition

@Serializable
data class DataShipControllerDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("EnableFirstPerson")
    override val enableFirstPerson: Boolean,
    @SerialName("EnableShipControl")
    override val enableShipControl: Boolean,
    @SerialName("EnableBuilderCockpit")
    override val enableBuilderCockpit: Boolean,
    @SerialName("IsDefault3rdView")
    override val isDefault3rdView: Boolean,
) : ShipControllerDefinition

@Serializable
data class DataCockpitDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("EnableFirstPerson")
    override val enableFirstPerson: Boolean,
    @SerialName("EnableShipControl")
    override val enableShipControl: Boolean,
    @SerialName("EnableBuilderCockpit")
    override val enableBuilderCockpit: Boolean,
    @SerialName("IsDefault3rdView")
    override val isDefault3rdView: Boolean,

    @SerialName("OxygenCapacity")
    override val oxygenCapacity: Float,
    @SerialName("IsPressurized")
    override val isPressurized: Boolean,
    @SerialName("HasInventory")
    override val hasInventory: Boolean,
) : CockpitDefinition

@Serializable
data class DataConveyorSorterDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("PowerInput")
    override val powerInput: Float,
) : ConveyorSorterDefinition

@Serializable
data class DataCryoChamberDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("OxygenCapacity")
    override val oxygenCapacity: Float,
    @SerialName("IsPressurized")
    override val isPressurized: Boolean,
    @SerialName("HasInventory")
    override val hasInventory: Boolean,
    @SerialName("EnableFirstPerson")
    override val enableFirstPerson: Boolean,
    @SerialName("EnableShipControl")
    override val enableShipControl: Boolean,
    @SerialName("EnableBuilderCockpit")
    override val enableBuilderCockpit: Boolean,
    @SerialName("IsDefault3rdView")
    override val isDefault3rdView: Boolean,

    @SerialName("IdlePowerConsumption")
    override val idlePowerConsumption: Float,
) : CryoChamberDefinition

@Serializable
data class DataDecoyDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("LightningRodRadiusLarge")
    override val lightningRodRadiusLarge: Float,
    @SerialName("LightningRodRadiusSmall")
    override val lightningRodRadiusSmall: Float,
) : DecoyDefinition

@Serializable
data class DataDoorDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MaxOpen")
    override val maxOpen: Float,
    @SerialName("OpeningSpeed")
    override val openingSpeed: Float,
) : DoorDefinition

@Serializable
data class DataExhaustBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
) : ExhaustBlockDefinition

@Serializable
data class DataFueledPowerProducerDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("MaxPowerOutput")
    override val maxPowerOutput: Float,

    @SerialName("FuelProductionToCapacityMultiplier")
    override val fuelProductionToCapacityMultiplier: Float,
) : FueledPowerProducerDefinition

@Serializable
data class DataGasFueledPowerProducerDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("FuelProductionToCapacityMultiplier")
    override val fuelProductionToCapacityMultiplier: Float,
    @SerialName("MaxPowerOutput")
    override val maxPowerOutput: Float,

    @SerialName("FuelCapacity")
    override val fuelCapacity: Float,
) : GasFueledPowerProducerDefinition

@Serializable
data class DataGasTankDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("InventoryMaxVolume")
    override val inventoryMaxVolume: Float,
    @SerialName("StandbyPowerConsumption")
    override val standbyPowerConsumption: Float,
    @SerialName("OperationalPowerConsumption")
    override val operationalPowerConsumption: Float,

    @SerialName("Capacity")
    override val capacity: Float,
) : GasTankDefinition

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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MinGravityAcceleration")
    override val minGravityAcceleration: Float,
    @SerialName("MaxGravityAcceleration")
    override val maxGravityAcceleration: Float,
) : GravityGeneratorBaseDefinition

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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("MinGravityAcceleration")
    override val minGravityAcceleration: Float,
    @SerialName("MaxGravityAcceleration")
    override val maxGravityAcceleration: Float,

    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
) : GravityGeneratorDefinition

@Serializable
data class DataGravityGeneratorSphereDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("MinGravityAcceleration")
    override val minGravityAcceleration: Float,
    @SerialName("MaxGravityAcceleration")
    override val maxGravityAcceleration: Float,

    @SerialName("MinRadius")
    override val minRadius: Float,
    @SerialName("MaxRadius")
    override val maxRadius: Float,
    @SerialName("BasePowerInput")
    override val basePowerInput: Float,
    @SerialName("ConsumptionPower")
    override val consumptionPower: Float,
) : GravityGeneratorSphereDefinition

@Serializable
data class DataGyroDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("ForceMagnitude")
    override val forceMagnitude: Float,
    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
) : GyroDefinition

@Serializable
data class DataSoundBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MinRange")
    override val minRange: Float,
    @SerialName("MaxRange")
    override val maxRange: Float,
    @SerialName("MaxLoopPeriod")
    override val maxLoopPeriod: Float,
    @SerialName("EmitterNumber")
    override val emitterNumber: Int,
    @SerialName("LoopUpdateThreshold")
    override val loopUpdateThreshold: Int,
) : SoundBlockDefinition

@Serializable
data class DataJumpDriveDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
    @SerialName("PowerNeededForJump")
    override val powerNeededForJump: Float,
    @SerialName("MinJumpDistance")
    override val minJumpDistance: Double,
    @SerialName("MaxJumpDistance")
    override val maxJumpDistance: Double,
    @SerialName("MaxJumpMass")
    override val maxJumpMass: Double,
) : JumpDriveDefinition

@Serializable
data class DataLandingGearDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MaxLockSeparatingVelocity")
    override val maxLockSeparatingVelocity: Float,
) : LandingGearDefinition

@Serializable
data class DataWeaponBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("InventoryMaxVolume")
    override val inventoryMaxVolume: Float,
    @SerialName("InventoryFillFactorMin")
    override val inventoryFillFactorMin: Float,
) : WeaponBlockDefinition

@Serializable
data class DataLightingBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
    @SerialName("ReflectorConeDegrees")
    override val reflectorConeDegrees: Float,
) : LightingBlockDefinition

@Serializable
data class DataMechanicalConnectionBlockBaseDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("SafetyDetach")
    override val safetyDetach: Float,
    @SerialName("SafetyDetachMin")
    override val safetyDetachMin: Float,
    @SerialName("SafetyDetachMax")
    override val safetyDetachMax: Float,
) : MechanicalConnectionBlockBaseDefinition

@Serializable
data class DataMedicalRoomDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("RespawnAllowed")
    override val respawnAllowed: Boolean,
    @SerialName("HealingAllowed")
    override val healingAllowed: Boolean,
    @SerialName("RefuelAllowed")
    override val refuelAllowed: Boolean,
    @SerialName("SuitChangeAllowed")
    override val suitChangeAllowed: Boolean,
    @SerialName("CustomWardrobesEnabled")
    override val customWardrobesEnabled: Boolean,
    @SerialName("ForceSuitChangeOnRespawn")
    override val forceSuitChangeOnRespawn: Boolean,
    @SerialName("SpawnWithoutOxygenEnabled")
    override val spawnWithoutOxygenEnabled: Boolean,
    @SerialName("WardrobeCharacterOffsetLength")
    override val wardrobeCharacterOffsetLength: Float,
) : MedicalRoomDefinition

@Serializable
data class DataMergeBlockDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("Strength")
    override val strength: Float,
) : MergeBlockDefinition

@Serializable
data class DataOreDetectorDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MaximumRange")
    override val maximumRange: Float,
) : OreDetectorDefinition

@Serializable
data class DataOxygenFarmDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("IsTwoSided")
    override val isTwoSided: Boolean,
    @SerialName("PanelOffset")
    override val panelOffset: Float,
    @SerialName("MaxGasOutput")
    override val maxGasOutput: Float,
    @SerialName("OperationalPowerConsumption")
    override val operationalPowerConsumption: Float,
) : OxygenFarmDefinition

@Serializable
data class DataOxygenGeneratorDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("InventoryMaxVolume")
    override val inventoryMaxVolume: Float,
    @SerialName("StandbyPowerConsumption")
    override val standbyPowerConsumption: Float,
    @SerialName("OperationalPowerConsumption")
    override val operationalPowerConsumption: Float,

    @SerialName("IceConsumptionPerSecond")
    override val iceConsumptionPerSecond: Float,
    @SerialName("IsOxygenOnly")
    override val isOxygenOnly: Boolean,
    @SerialName("InventoryFillFactorMin")
    override val inventoryFillFactorMin: Float,
    @SerialName("InventoryFillFactorMax")
    override val inventoryFillFactorMax: Float,
    @SerialName("FuelPullAmountFromConveyorInMinutes")
    override val fuelPullAmountFromConveyorInMinutes: Float,
) : OxygenGeneratorDefinition

@Serializable
data class DataParachuteDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("PowerConsumptionIdle")
    override val powerConsumptionIdle: Float,
    @SerialName("PowerConsumptionMoving")
    override val powerConsumptionMoving: Float,
    @SerialName("DragCoefficient")
    override val dragCoefficient: Float,
    @SerialName("ReefAtmosphereLevel")
    override val reefAtmosphereLevel: Float,
    @SerialName("MinimumAtmosphereLevel")
    override val minimumAtmosphereLevel: Float,
    @SerialName("RadiusMultiplier")
    override val radiusMultiplier: Float,
) : ParachuteDefinition

@Serializable
data class DataPistonBaseDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,

    @SerialName("SafetyDetach")
    override val safetyDetach: Float,
    @SerialName("SafetyDetachMin")
    override val safetyDetachMin: Float,
    @SerialName("SafetyDetachMax")
    override val safetyDetachMax: Float,

    @SerialName("Minimum")
    override val minimum: Float,
    @SerialName("Maximum")
    override val maximum: Float,
    @SerialName("MaxVelocity")
    override val maxVelocity: Float,
    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
    @SerialName("MaxImpulse")
    override val maxImpulse: Float,
    @SerialName("DefaultMaxImpulseAxis")
    override val defaultMaxImpulseAxis: Float,
    @SerialName("DefaultMaxImpulseNonAxis")
    override val defaultMaxImpulseNonAxis: Float,
    @SerialName("UnsafeImpulseThreshold")
    override val unsafeImpulseThreshold: Float,
) : PistonBaseDefinition

@Serializable
data class DataProjectorDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("RequiredPowerInput")
    override val requiredPowerInput: Float,
    @SerialName("AllowScaling")
    override val allowScaling: Boolean,
    @SerialName("AllowWelding")
    override val allowWelding: Boolean,
    @SerialName("IgnoreSize")
    override val ignoreSize: Boolean,
    @SerialName("RotationAngleStepDeg")
    override val rotationAngleStepDeg: Int,
) : ProjectorDefinition

@Serializable
data class DataRadioAntennaDefinition(
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
    @SerialName("Components")
    override val components: List<Component>,



    @SerialName("MaxBroadcastRadius")
    override val maxBroadcastRadius: Float,
    @SerialName("LightningRodRadiusLarge")
    override val lightningRodRadiusLarge: Float,
    @SerialName("LightningRodRadiusSmall")
    override val lightningRodRadiusSmall: Float,
) : RadioAntennaDefinition

