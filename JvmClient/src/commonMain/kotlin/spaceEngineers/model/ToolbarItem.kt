package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToolbarItem(
    @SerialName("Type")
    val type: String,
    @SerialName("SubType")
    val subType: String,
    @SerialName("Name")
    val name: String
) {

    override fun toString(): String {
        return name
    }
}
