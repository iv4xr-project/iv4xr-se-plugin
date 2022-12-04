package spaceEngineers.model

// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataTerminalBlock(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,



    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,
) : TerminalBlock

@Serializable
data class DataFunctionalBlock(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("Enabled")
    override val enabled: Boolean,
) : FunctionalBlock

@Serializable
data class DataDoorBase(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("Open")
    override val open: Boolean,
    @SerialName("AnyoneCanUse")
    override val anyoneCanUse: Boolean,
) : DoorBase

@Serializable
data class DataFueledPowerProducer(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("MaxOutput")
    override val maxOutput: Float,
    @SerialName("CurrentOutput")
    override val currentOutput: Float,
    @SerialName("Capacity")
    override val capacity: Float,
) : FueledPowerProducer

@Serializable
data class DataWarhead(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("IsCountingDown")
    override val isCountingDown: Boolean,
    @SerialName("IsArmed")
    override val isArmed: Boolean,
) : Warhead

@Serializable
data class DataMedicalRoom(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("SuitChangeAllowed")
    override val suitChangeAllowed: Boolean,
    @SerialName("CustomWardrobesEnabled")
    override val customWardrobesEnabled: Boolean,
    @SerialName("SpawnName")
    override val spawnName: String,
    @SerialName("RespawnAllowed")
    override val respawnAllowed: Boolean,
    @SerialName("RefuelAllowed")
    override val refuelAllowed: Boolean,
    @SerialName("HealingAllowed")
    override val healingAllowed: Boolean,
    @SerialName("SpawnWithoutOxygenEnabled")
    override val spawnWithoutOxygenEnabled: Boolean,
    @SerialName("ForceSuitChangeOnRespawn")
    override val forceSuitChangeOnRespawn: Boolean,
) : MedicalRoom

@Serializable
data class DataTimerBlock(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("Silent")
    override val silent: Boolean,
    @SerialName("TriggerDelay")
    override val triggerDelay: Float,
    @SerialName("Toolbar")
    override val toolbar: Toolbar,
) : TimerBlock

@Serializable
data class DataSensorBlock(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("IsActive")
    override val isActive: Boolean,
    @SerialName("FieldMin")
    override val fieldMin: Vec3F,
    @SerialName("FieldMax")
    override val fieldMax: Vec3F,
    @SerialName("MaxRange")
    override val maxRange: Float,
    @SerialName("Filters")
    override val filters: Int,
    @SerialName("Toolbar")
    override val toolbar: Toolbar,
) : SensorBlock

@Serializable
data class DataGravityGenerator(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("GravityAcceleration")
    override val gravityAcceleration: Float,
    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("FieldSize")
    override val fieldSize: Vec3F,
) : GravityGenerator

@Serializable
data class DataGravityGeneratorSphere(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("GravityAcceleration")
    override val gravityAcceleration: Float,
    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("Radius")
    override val radius: Float,
) : GravityGeneratorSphere

@Serializable
data class DataGravityGeneratorBase(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
    @SerialName("CustomName")
    override val customName: String,
    @SerialName("CustomData")
    override val customData: String,

    @SerialName("GravityAcceleration")
    override val gravityAcceleration: Float,
) : GravityGeneratorBase

