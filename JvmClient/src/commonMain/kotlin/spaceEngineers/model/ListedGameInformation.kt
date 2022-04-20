package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListedGameInformation(
    @SerialName("World")
    val world: String,
    @SerialName("Server")
    val server: String,
)
