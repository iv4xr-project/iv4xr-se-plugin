package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveAsData(
    @SerialName("Name")
    val name: String,
)
