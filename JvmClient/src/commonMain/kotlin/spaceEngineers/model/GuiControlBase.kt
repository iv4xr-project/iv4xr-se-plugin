package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val GuiControlBase?.canUse: Boolean
    get() = this != null && enabled && visible

@Serializable
data class GuiControlBase(
    @SerialName("Enabled")
    val enabled: Boolean,
    @SerialName("Visible")
    val visible: Boolean,
    @SerialName("Name")
    val name: String,
)
