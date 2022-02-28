package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageBoxData(
    @SerialName("Caption")
    val caption: String,
    @SerialName("Text")
    val text: String,
    @SerialName("ButtonType")
    val buttonType: Int,
)
