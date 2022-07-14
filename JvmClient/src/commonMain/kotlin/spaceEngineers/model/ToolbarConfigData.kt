package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToolbarConfigData(
    @SerialName("SearchText")
    val searchText: String,
    @SerialName("GridItems")
    val gridItems: List<DefinitionId?>,
    @SerialName("Categories")
    val categories: List<String>,
    @SerialName("SelectedCategories")
    val selectedCategories: List<String>,
)
