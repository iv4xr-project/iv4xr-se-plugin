package testrail.model

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentsForCase(
    val offset: Long,
    val limit: Long,
    val size: Long,
    val attachments: List<Attachment>,
)
