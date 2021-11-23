package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Observation(
    @SerialName("Character")
    val character: CharacterObservation,
    @SerialName("Grids")
    val grids: List<CubeGrid> = emptyList(),
)
