package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicalsData(
    @SerialName("MedicalRooms")
    val medicalRooms: List<MedicalRoom>,
    @SerialName("Factions")
    val factions: List<Faction>,
    @SerialName("ShowFactions")
    var showFactions: Boolean,
    @SerialName("ShowMotD")
    var showMotD: Boolean,
    @SerialName("IsMotdOpen")
    var isMotdOpen: Boolean,
    @SerialName("Paused")
    var paused: Boolean,
    @SerialName("IsMultiplayerReady")
    var isMultiplayerReady: Boolean,
)

