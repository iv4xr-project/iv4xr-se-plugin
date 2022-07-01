package testrail.model

import kotlinx.serialization.Serializable

@Serializable
data class Cases(
    val offset: Long,
    val limit: Long,
    val size: Long,
    val cases: List<Case>,
)
