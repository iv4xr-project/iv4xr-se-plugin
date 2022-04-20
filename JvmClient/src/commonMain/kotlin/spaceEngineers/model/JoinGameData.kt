package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class JoinGameData(
    @SerialName("SelectedTab")
    val selectedTab: String,
    @SerialName("Games")
    val games: List<ListedGameInformation>,

    )
