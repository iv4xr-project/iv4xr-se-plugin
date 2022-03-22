package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DebugInfo(
    @SerialName("IsDedicated")
    val isDedicated: Boolean,
    @SerialName("Executable")
    val executable: String,
    @SerialName("Version")
    val version: Int,
    @SerialName("MachineName")
    val machineName: String,
    @SerialName("SessionReady")
    val sessionReady: Boolean,
)
