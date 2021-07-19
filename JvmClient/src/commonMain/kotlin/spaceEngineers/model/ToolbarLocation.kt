package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToolbarLocation(
    @SerialName("Slot")
    val slot: Int,
    @SerialName("Page")
    val page: Int
) {
    companion object {
        fun fromIndex(index: Int, pageSize: Int = 10): ToolbarLocation {
            return ToolbarLocation(
                slot = index % pageSize,
                page = index / pageSize
            )
        }
    }
}
