package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionSettings(
    @SerialName("InfiniteAmmo")
    val infiniteAmmo: Boolean,
    @SerialName("GameMode")
    val gameMode: GameMode,
)
