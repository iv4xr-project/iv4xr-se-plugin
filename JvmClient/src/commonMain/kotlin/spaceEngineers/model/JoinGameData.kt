package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class JoinGameData(
    @SerialName("SelectedTab")
    val selectedTab: Int,
    @SerialName("SelectedTabName")
    val selectedTabName: String,
    @SerialName("Games")
    val games: List<ListedGameInformation>,
    @SerialName("JoinWorldButton")
    val joinWorldButton: GuiControlBase,
)
