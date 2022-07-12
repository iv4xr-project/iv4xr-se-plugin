package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SessionInfo(
    @SerialName("Name")
    var name: String,
    @SerialName("Description")
    var description: String? = null,
    @SerialName("CurrentPath")
    var currentPath: String,
    @SerialName("IsAdminMenuEnabled")
    var isAdminMenuEnabled: Boolean,
    @SerialName("IsRunningExperimental")
    var isRunningExperimental: Boolean,
    @SerialName("Ready")
    var ready: Boolean,
    @SerialName("IsUnloading")
    var isUnloading: Boolean,
    @SerialName("IsCopyPastingEnabled")
    var isCopyPastingEnabled: Boolean,
    @SerialName("StreamingInProgress")
    var streamingInProgress: Boolean,
    @SerialName("IsServer")
    var isServer: Boolean,
    @SerialName("IsPausable")
    var isPausable: Boolean,
    @SerialName("GameDefinition")
    var gameDefinition: DefinitionId,
    @SerialName("Settings")
    var settings: SessionSettings,
)

