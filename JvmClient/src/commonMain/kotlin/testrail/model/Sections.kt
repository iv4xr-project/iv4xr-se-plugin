package testrail.model

import kotlinx.serialization.Serializable

@Serializable
data class Sections(
    val offset: Long,
    val limit: Long,
    val size: Long,
    val sections: List<Section>,
)
