package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildProgressModel(
    /**
     * Percentage of maximum integrity.
     */
    @SerialName("BuildRatioUpperBound")
    val buildRatioUpperBound: Float,
)
