package spaceEngineers.model

data class Observation(
    val character: CharacterObservation,
    val grids: List<CubeGrid> = emptyList(),
)
