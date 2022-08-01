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
    val showFactions: Boolean,
    @SerialName("ShowMotD")
    val showMotD: Boolean,
    @SerialName("IsMotdOpen")
    val isMotdOpen: Boolean,
    @SerialName("Paused")
    val paused: Boolean,
    @SerialName("IsMultiplayerReady")
    val isMultiplayerReady: Boolean,
    @SerialName("RespawnButton")
    val respawnButton: GuiControlBase?,
)

