package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicalRoomData(
    @SerialName("Name")
    val name: String,
    @SerialName("AvailableIn")
    val availableIn: String,
)
