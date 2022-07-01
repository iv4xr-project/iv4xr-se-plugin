package testrail.model

import kotlinx.serialization.Serializable

@Serializable
data class Case(
    val id: Long,
    val title: String?,
    val section_id: Long,
    val template_id: Long,
    val type_id: Long,
    val priority_id: Long,
    val custom_preconds: String?,
    val suite_id: Long,
    val display_order: Long,
    val is_deleted: Long,
)
