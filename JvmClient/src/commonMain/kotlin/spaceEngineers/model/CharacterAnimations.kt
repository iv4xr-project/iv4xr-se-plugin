package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterAnimations(
    @SerialName("AnimationsPerLayer")
    val animationsPerLayer: Map<String, String>,
)
