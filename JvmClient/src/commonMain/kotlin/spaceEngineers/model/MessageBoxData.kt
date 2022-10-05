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
) {

    companion object {
        val NONE = 0
        val OK = 1
        val YES_NO = 2
        val YES_NO_CANCEL = 3
        val YES_NO_TIMEOUT = 4
        val NONE_TIMEOUT = 5
    }
}
