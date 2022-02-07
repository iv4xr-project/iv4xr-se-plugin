package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Faction(
    @SerialName("Tag")
    val tag: String,
    @SerialName("Name")
    val name: String,
)
