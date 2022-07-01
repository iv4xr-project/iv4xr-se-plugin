package testrail.model

import kotlinx.serialization.Serializable

@Serializable
data class Section(
    val id: Long,
    val name: String,
    val description: String?,
    val suite_id: Long,
    val parent_id: Long?,
    val display_order: Int,
    val depth: Int,
)
