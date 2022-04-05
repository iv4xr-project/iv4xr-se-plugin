package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToolbarItem(
    @SerialName("Id")
    val id: DefinitionId,
    @SerialName("Name")
    val name: String
) {

    override fun toString(): String {
        return "$id - $name"
    }
}
