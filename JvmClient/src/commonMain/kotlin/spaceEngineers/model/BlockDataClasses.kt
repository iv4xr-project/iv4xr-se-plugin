package spaceEngineers.model

// Generated file using BlockMappingGenerator.kt.

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataTerminalBlock(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("BlockType")
    override val blockType: String,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3,
    @SerialName("Size")
    override val size: Vec3,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,



    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,
) : TerminalBlock

@Serializable
data class DataFunctionalBlock(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("BlockType")
    override val blockType: String,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3,
    @SerialName("Size")
    override val size: Vec3,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,

    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,

    @SerialName("Enabled")
    override val enabled: Boolean,
) : FunctionalBlock

@Serializable
data class DataDoorBase(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("BlockType")
    override val blockType: String,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3,
    @SerialName("Size")
    override val size: Vec3,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,

    @SerialName("Open")
    override val open: Boolean,
    @SerialName("AnyoneCanUse")
    override val anyoneCanUse: Boolean,
) : DoorBase

@Serializable
data class DataFueledPowerProducer(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("BlockType")
    override val blockType: String,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3,
    @SerialName("Size")
    override val size: Vec3,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,

    @SerialName("Enabled")
    override val enabled: Boolean,
    @SerialName("ShowInInventory")
    override val showInInventory: Boolean,
    @SerialName("ShowInTerminal")
    override val showInTerminal: Boolean,
    @SerialName("ShowOnHUD")
    override val showOnHUD: Boolean,

    @SerialName("MaxOutput")
    override val maxOutput: Float,
    @SerialName("CurrentOutput")
    override val currentOutput: Float,
    @SerialName("Capacity")
    override val capacity: Float,
) : FueledPowerProducer

