package testrail.model

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: String,
    val name: String,
    val size: Long,
    val user_id: Long,
    val project_id: Long,
)
