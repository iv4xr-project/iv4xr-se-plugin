package testrail.model

import kotlinx.serialization.Serializable

@Serializable
data class Suite(
    val id: Long,
    val name: String,
    val description: String?,
    val project_id: Long,
)
