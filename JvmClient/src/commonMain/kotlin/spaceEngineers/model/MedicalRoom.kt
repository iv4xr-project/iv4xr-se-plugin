package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicalRoom(
    @SerialName("Name")
    val name: String,
    @SerialName("AvailableIn")
    val availableIn: String,
)
