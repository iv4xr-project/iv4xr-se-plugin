package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CameraController(
    @SerialName("IsInFirstPersonView")
    val isInFirstPersonView: Boolean,
    @SerialName("ForceFirstPersonCamera")
    val forceFirstPersonCamera: Boolean,
    @SerialName("CameraControllerEnum")
    val cameraControllerEnum: Int
)

@Serializable
data class SessionInfo(
    @SerialName("Name")
    val name: String,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("CurrentPath")
    val currentPath: String,
    @SerialName("IsAdminMenuEnabled")
    val isAdminMenuEnabled: Boolean,
    @SerialName("IsRunningExperimental")
    val isRunningExperimental: Boolean,
    @SerialName("Ready")
    val ready: Boolean,
    @SerialName("IsUnloading")
    val isUnloading: Boolean,
    @SerialName("IsCopyPastingEnabled")
    val isCopyPastingEnabled: Boolean,
    @SerialName("StreamingInProgress")
    val streamingInProgress: Boolean,
    @SerialName("IsServer")
    val isServer: Boolean,
    @SerialName("IsPausable")
    val isPausable: Boolean,
    @SerialName("GameDefinition")
    val gameDefinition: DefinitionId,
    @SerialName("Settings")
    val settings: SessionSettings,
    @SerialName("Camera")
    val camera: CameraController? = null,
)
