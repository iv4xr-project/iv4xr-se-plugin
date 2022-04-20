package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerConnectData(
    @SerialName("Address")
    val address: String,
    @SerialName("AddServerToFavorites")
    val addServerToFavorites: Boolean,
)
