package spaceEngineers.model

// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.

interface AirtightDoorGenericDefinition: BlockDefinition  {
    val powerConsumptionIdle: Float
    val powerConsumptionMoving: Float
    val openingSpeed: Float
}

interface LCDPanelsBlockDefinition: BlockDefinition  {
    val requiredPowerInput: Float
}

interface PowerProducerDefinition: BlockDefinition  {
    val maxPowerOutput: Float
}

interface AdvancedDoorDefinition: BlockDefinition  {
    val powerConsumptionIdle: Float
    val powerConsumptionMoving: Float
}

interface AirVentDefinition: BlockDefinition  {
    val standbyPowerConsumption: Float
    val operationalPowerConsumption: Float
    val ventilationCapacityPerSecond: Float
}

interface ProductionBlockDefinition: BlockDefinition  {
    val inventoryMaxVolume: Float
    val standbyPowerConsumption: Float
    val operationalPowerConsumption: Float
}

interface AssemblerDefinition: ProductionBlockDefinition  {
    val assemblySpeed: Float
}

interface BatteryBlockDefinition: PowerProducerDefinition  {
    val maxStoredPower: Float
    val initialStoredPowerRatio: Float
    val requiredPowerInput: Float
    val adaptibleInput: Boolean
}

interface BeaconDefinition: BlockDefinition  {
    val maxBroadcastRadius: Float
    val maxBroadcastPowerDrainkW: Float
}

interface ButtonPanelDefinition: BlockDefinition  {
    val buttonCount: Int
}

interface CameraBlockDefinition: BlockDefinition  {
    val requiredPowerInput: Float
    val requiredChargingInput: Float
    val minFov: Float
    val maxFov: Float
    val raycastConeLimit: Float
    val raycastDistanceLimit: Double
    val raycastTimeMultiplier: Float
}

interface ShipControllerDefinition: BlockDefinition  {
    val enableFirstPerson: Boolean
    val enableShipControl: Boolean
    val enableBuilderCockpit: Boolean
    val isDefault3rdView: Boolean
}

interface CockpitDefinition: ShipControllerDefinition  {
    val oxygenCapacity: Float
    val isPressurized: Boolean
    val hasInventory: Boolean
}

interface ConveyorSorterDefinition: BlockDefinition  {
    val powerInput: Float
}

interface CryoChamberDefinition: CockpitDefinition  {
    val idlePowerConsumption: Float
}

interface DecoyDefinition: BlockDefinition  {
    val lightningRodRadiusLarge: Float
    val lightningRodRadiusSmall: Float
}

interface DoorDefinition: BlockDefinition  {
    val maxOpen: Float
    val openingSpeed: Float
}

interface ExhaustBlockDefinition: BlockDefinition  {
    val requiredPowerInput: Float
}

interface FueledPowerProducerDefinition: PowerProducerDefinition  {
    val fuelProductionToCapacityMultiplier: Float
}

interface GasFueledPowerProducerDefinition: FueledPowerProducerDefinition  {
    val fuelCapacity: Float
}

interface GasTankDefinition: ProductionBlockDefinition  {
    val capacity: Float
}

interface GravityGeneratorBaseDefinition: BlockDefinition  {
    val minGravityAcceleration: Float
    val maxGravityAcceleration: Float
}

interface GravityGeneratorDefinition: GravityGeneratorBaseDefinition  {
    val requiredPowerInput: Float
}

interface GravityGeneratorSphereDefinition: GravityGeneratorBaseDefinition  {
    val minRadius: Float
    val maxRadius: Float
    val basePowerInput: Float
    val consumptionPower: Float
}

interface GyroDefinition: BlockDefinition  {
    val forceMagnitude: Float
    val requiredPowerInput: Float
}

interface SoundBlockDefinition: BlockDefinition  {
    val minRange: Float
    val maxRange: Float
    val maxLoopPeriod: Float
    val emitterNumber: Int
    val loopUpdateThreshold: Int
}

interface JumpDriveDefinition: BlockDefinition  {
    val requiredPowerInput: Float
    val powerNeededForJump: Float
    val minJumpDistance: Double
    val maxJumpDistance: Double
    val maxJumpMass: Double
}

interface LandingGearDefinition: BlockDefinition  {
    val maxLockSeparatingVelocity: Float
}

interface WeaponBlockDefinition: BlockDefinition  {
    val inventoryMaxVolume: Float
    val inventoryFillFactorMin: Float
}

interface LightingBlockDefinition: BlockDefinition  {
    val requiredPowerInput: Float
    val reflectorConeDegrees: Float
}

interface MechanicalConnectionBlockBaseDefinition: BlockDefinition  {
    val safetyDetach: Float
    val safetyDetachMin: Float
    val safetyDetachMax: Float
}

interface MedicalRoomDefinition: BlockDefinition  {
    val respawnAllowed: Boolean
    val healingAllowed: Boolean
    val refuelAllowed: Boolean
    val suitChangeAllowed: Boolean
    val customWardrobesEnabled: Boolean
    val forceSuitChangeOnRespawn: Boolean
    val spawnWithoutOxygenEnabled: Boolean
    val wardrobeCharacterOffsetLength: Float
}

interface MergeBlockDefinition: BlockDefinition  {
    val strength: Float
}

interface OreDetectorDefinition: BlockDefinition  {
    val maximumRange: Float
}

interface OxygenFarmDefinition: BlockDefinition  {
    val isTwoSided: Boolean
    val panelOffset: Float
    val maxGasOutput: Float
    val operationalPowerConsumption: Float
}

interface OxygenGeneratorDefinition: ProductionBlockDefinition  {
    val iceConsumptionPerSecond: Float
    val isOxygenOnly: Boolean
    val inventoryFillFactorMin: Float
    val inventoryFillFactorMax: Float
    val fuelPullAmountFromConveyorInMinutes: Float
}

interface ParachuteDefinition: BlockDefinition  {
    val powerConsumptionIdle: Float
    val powerConsumptionMoving: Float
    val dragCoefficient: Float
    val reefAtmosphereLevel: Float
    val minimumAtmosphereLevel: Float
    val radiusMultiplier: Float
}

interface PistonBaseDefinition: MechanicalConnectionBlockBaseDefinition  {
    val minimum: Float
    val maximum: Float
    val maxVelocity: Float
    val requiredPowerInput: Float
    val maxImpulse: Float
    val defaultMaxImpulseAxis: Float
    val defaultMaxImpulseNonAxis: Float
    val unsafeImpulseThreshold: Float
}

interface ProjectorDefinition: BlockDefinition  {
    val requiredPowerInput: Float
    val allowScaling: Boolean
    val allowWelding: Boolean
    val ignoreSize: Boolean
    val rotationAngleStepDeg: Int
}

interface RadioAntennaDefinition: BlockDefinition  {
    val maxBroadcastRadius: Float
    val lightningRodRadiusLarge: Float
    val lightningRodRadiusSmall: Float
}

