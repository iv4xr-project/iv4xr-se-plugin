package spaceEngineers.controller

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.DataBlockDefinition
import spaceEngineers.model.generatedBlockDefinitionSerializerMappings
import spaceEngineers.util.generator.removeDefinitionPrefix
import kotlin.reflect.KClass

val blockDefinitionMappings = mapOf<String, Map<String, KClass<*>>>(
    "AirtightDoorGenericDefinition" to mapOf(
        "PowerConsumptionIdle" to Float::class,
        "PowerConsumptionMoving" to Float::class,
        "OpeningSpeed" to Float::class,
    ),
    "LCDPanelsBlockDefinition" to mapOf(
        "RequiredPowerInput" to Float::class
    ),
    "PowerProducerDefinition" to mapOf(
        "MaxPowerOutput" to Float::class,
    ),
    "AdvancedDoorDefinition" to mapOf(
        "PowerConsumptionIdle" to Float::class,
        "PowerConsumptionMoving" to Float::class,
    ),
    "AirVentDefinition" to mapOf(
        "StandbyPowerConsumption" to Float::class,
        "OperationalPowerConsumption" to Float::class,
        "VentilationCapacityPerSecond" to Float::class,
    ),
    "ProductionBlockDefinition" to mapOf(
        "InventoryMaxVolume" to Float::class,
        "StandbyPowerConsumption" to Float::class,
        "OperationalPowerConsumption" to Float::class,
        // "InventorySize" to Vec3F::class,
    ),
    "AssemblerDefinition" to mapOf(
        "AssemblySpeed" to Float::class,
    ),
    "BatteryBlockDefinition" to mapOf(
        "MaxStoredPower" to Float::class,
        "InitialStoredPowerRatio" to Float::class,
        "RequiredPowerInput" to Float::class,
        "AdaptibleInput" to Boolean::class,
    ),
    "BeaconDefinition" to mapOf(
        "MaxBroadcastRadius" to Float::class,
        "MaxBroadcastPowerDrainkW" to Float::class,
    ),
    "ButtonPanelDefinition" to mapOf(
        "ButtonCount" to Int::class,
    ),
    "CameraBlockDefinition" to mapOf(
        "RequiredPowerInput" to Float::class,
        "RequiredChargingInput" to Float::class,
        "MinFov" to Float::class,
        "MaxFov" to Float::class,
        "RaycastConeLimit" to Float::class,
        "RaycastDistanceLimit" to Double::class,
        "RaycastTimeMultiplier" to Float::class,
    ),
    "ShipControllerDefinition" to mapOf(
        "EnableFirstPerson" to Boolean::class,
        "EnableShipControl" to Boolean::class,
        "EnableBuilderCockpit" to Boolean::class,
        "IsDefault3rdView" to Boolean::class,
    ),
    "CockpitDefinition" to mapOf(
        "OxygenCapacity" to Float::class,
        "IsPressurized" to Boolean::class,
        "HasInventory" to Boolean::class,
    ),
    "ConveyorSorterDefinition" to mapOf(
        "PowerInput" to Float::class,
    ),
    "CryoChamberDefinition" to mapOf(
        "IdlePowerConsumption" to Float::class,
    ),
    "DecoyDefinition" to mapOf(
        "LightningRodRadiusLarge" to Float::class,
        "LightningRodRadiusSmall" to Float::class,
    ),
    "DoorDefinition" to mapOf(
        "MaxOpen" to Float::class,
        "OpeningSpeed" to Float::class,
    ),
    "ExhaustBlockDefinition" to mapOf(
        "RequiredPowerInput" to Float::class,
    ),
    "FueledPowerProducerDefinition" to mapOf(
        "FuelProductionToCapacityMultiplier" to Float::class,
    ),
    "GasFueledPowerProducerDefinition" to mapOf(
        "FuelCapacity" to Float::class,
    ),
    "GasTankDefinition" to mapOf(
        "Capacity" to Float::class,
    ),
    "GravityGeneratorBaseDefinition" to mapOf(
        "MinGravityAcceleration" to Float::class,
        "MaxGravityAcceleration" to Float::class,
    ),
    "GravityGeneratorDefinition" to mapOf(
        "RequiredPowerInput" to Float::class,
    ),
    "GravityGeneratorSphereDefinition" to mapOf(
        "MinRadius" to Float::class,
        "MaxRadius" to Float::class,
        "BasePowerInput" to Float::class,
        "ConsumptionPower" to Float::class,
    ),
    "GyroDefinition" to mapOf(
        "ForceMagnitude" to Float::class,
        "RequiredPowerInput" to Float::class,
    ),
    "SoundBlockDefinition" to mapOf(
        "MinRange" to Float::class,
        "MaxRange" to Float::class,
        "MaxLoopPeriod" to Float::class,
        "EmitterNumber" to Int::class,
        "LoopUpdateThreshold" to Int::class,
    ),
    "JumpDriveDefinition" to mapOf(
        "RequiredPowerInput" to Float::class,
        "PowerNeededForJump" to Float::class,
        "MinJumpDistance" to Double::class,
        "MaxJumpDistance" to Double::class,
        "MaxJumpMass" to Double::class,
    ),
    "LandingGearDefinition" to mapOf(
        "MaxLockSeparatingVelocity" to Float::class,
    ),
    "WeaponBlockDefinition" to mapOf(
        "InventoryMaxVolume" to Float::class,
        "InventoryFillFactorMin" to Float::class,
    ),
    "LightingBlockDefinition" to mapOf(
        "RequiredPowerInput" to Float::class,
        "ReflectorConeDegrees" to Float::class,
    ),
    "MechanicalConnectionBlockBaseDefinition" to mapOf(
        "SafetyDetach" to Float::class,
        "SafetyDetachMin" to Float::class,
        "SafetyDetachMax" to Float::class,
    ),
    "MedicalRoomDefinition" to mapOf(
        "RespawnAllowed" to Boolean::class,
        "HealingAllowed" to Boolean::class,
        "RefuelAllowed" to Boolean::class,
        "SuitChangeAllowed" to Boolean::class,
        "CustomWardrobesEnabled" to Boolean::class,
        "ForceSuitChangeOnRespawn" to Boolean::class,
        "SpawnWithoutOxygenEnabled" to Boolean::class,
        "WardrobeCharacterOffsetLength" to Float::class,
    ),
    "MergeBlockDefinition" to mapOf(
        "Strength" to Float::class,
    ),
    "OreDetectorDefinition" to mapOf(
        "MaximumRange" to Float::class,
    ),
    "OxygenFarmDefinition" to mapOf(
        "IsTwoSided" to Boolean::class,
        "PanelOffset" to Float::class,
        "MaxGasOutput" to Float::class,
        "OperationalPowerConsumption" to Float::class,
    ),
    "OxygenGeneratorDefinition" to mapOf(
        "IceConsumptionPerSecond" to Float::class,
        "IsOxygenOnly" to Boolean::class,
        "InventoryFillFactorMin" to Float::class,
        "InventoryFillFactorMax" to Float::class,
        "FuelPullAmountFromConveyorInMinutes" to Float::class,
    ),
    "ParachuteDefinition" to mapOf(
        "PowerConsumptionIdle" to Float::class,
        "PowerConsumptionMoving" to Float::class,
        "DragCoefficient" to Float::class,
        "ReefAtmosphereLevel" to Float::class,
        "MinimumAtmosphereLevel" to Float::class,
        "RadiusMultiplier" to Float::class,
    ),
    "PistonBaseDefinition" to mapOf(
        "Minimum" to Float::class,
        "Maximum" to Float::class,
        "MaxVelocity" to Float::class,
        "RequiredPowerInput" to Float::class,
        "MaxImpulse" to Float::class,
        "DefaultMaxImpulseAxis" to Float::class,
        "DefaultMaxImpulseNonAxis" to Float::class,
        "UnsafeImpulseThreshold" to Float::class,
    ),
    "ProjectorDefinition" to mapOf(
        "RequiredPowerInput" to Float::class,
        "AllowScaling" to Boolean::class,
        "AllowWelding" to Boolean::class,
        "IgnoreSize" to Boolean::class,
        "RotationAngleStepDeg" to Int::class,
    ),
    "RadioAntennaDefinition" to mapOf(
        "MaxBroadcastRadius" to Float::class,
        "LightningRodRadiusLarge" to Float::class,
        "LightningRodRadiusSmall" to Float::class,
    ),
)

object BlockDefinitionSerializer : JsonContentPolymorphicSerializer<BlockDefinition>(BlockDefinition::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out BlockDefinition> {
        // val id = element.jsonObject["DefinitionId"]!!.jsonObject["Id"]!!.jsonPrimitive.content.removeBuilderPrefix()
        val type = element.jsonObject["Type"]!!.jsonPrimitive.content.removeDefinitionPrefix()
        return generatedBlockDefinitionSerializerMappings[type] ?: DataBlockDefinition.serializer()
    }
}
