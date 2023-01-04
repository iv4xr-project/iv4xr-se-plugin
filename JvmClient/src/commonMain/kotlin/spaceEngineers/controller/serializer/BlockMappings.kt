package spaceEngineers.controller.serializer

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import spaceEngineers.model.Toolbar
import spaceEngineers.model.Vec3F
import spaceEngineers.util.generator.removeBuilderPrefix
import kotlin.reflect.KClass

val blockMappings = mapOf<String, Map<String, KClass<*>>>(
    "TerminalBlock" to mapOf(
        "ShowInInventory" to Boolean::class,
        "ShowInTerminal" to Boolean::class,
        "ShowOnHUD" to Boolean::class,
        "CustomName" to String::class,
        "CustomData" to String::class,
    ),
    "FunctionalBlock" to mapOf(
        "Enabled" to Boolean::class
    ),
    "DoorBase" to mapOf(
        "Open" to Boolean::class,
        "AnyoneCanUse" to Boolean::class
    ),
    "FueledPowerProducer" to mapOf(
        "MaxOutput" to Float::class,
        "CurrentOutput" to Float::class,
        "Capacity" to Float::class,
    ),
    "Warhead" to mapOf(
        "IsCountingDown" to Boolean::class,
        "IsArmed" to Boolean::class,
    ),
    "MedicalRoom" to mapOf(
        "SuitChangeAllowed" to Boolean::class,
        "CustomWardrobesEnabled" to Boolean::class,
        "SpawnName" to String::class,
        "RespawnAllowed" to Boolean::class,
        "RefuelAllowed" to Boolean::class,
        "HealingAllowed" to Boolean::class,
        "SpawnWithoutOxygenEnabled" to Boolean::class,
        "ForceSuitChangeOnRespawn" to Boolean::class,
    ),
    "TimerBlock" to mapOf(
        "Silent" to Boolean::class,
        "TriggerDelay" to Float::class,
        "Toolbar" to Toolbar::class,
    ),
    "SensorBlock" to mapOf(
        "IsActive" to Boolean::class,
        "FieldMin" to Vec3F::class,
        "FieldMax" to Vec3F::class,
        "MaxRange" to Float::class,
        "Filters" to Int::class,
        "Toolbar" to Toolbar::class,
    ),
    "GravityGenerator" to mapOf(
        "FieldSize" to Vec3F::class,
    ),
    "GravityGeneratorSphere" to mapOf(
        "Radius" to Float::class,
    ),
    "GravityGeneratorBase" to mapOf(
        "GravityAcceleration" to Float::class,
    ),
    "MechanicalConnectionBlockBase" to mapOf(
        "SafetyDetach" to Float::class,
    ),
    "PistonBase" to mapOf(
        "CurrentPosition" to Float::class,
        "Status" to Int::class,
        "Velocity" to Float::class,
        "MinLimit" to Float::class,
        "MaxLimit" to Float::class,
        "MaxImpulseAxis" to Float::class,
        "MaxImpulseNonAxis" to Float::class,
    ),
    "Thrust" to mapOf(
        "ThrustOverride" to Float::class,
    ),
)

internal fun getDefinitionIdId(element: JsonElement, key: String = "DefinitionId"): String? {
    return element.jsonObject[key]?.jsonObject?.get("Id")?.jsonPrimitive?.content?.removeBuilderPrefix()
}
