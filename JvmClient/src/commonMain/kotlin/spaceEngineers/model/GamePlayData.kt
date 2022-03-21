package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PointOfInterest(
    @SerialName("Position")
    val position: Vec3F,
    @SerialName("Type")
    val type: Int,
    @SerialName("Relationship")
    val relationship: Int,
    @SerialName("Text")
    val text: String,
    @SerialName("Distance")
    val distance: Double,
    @SerialName("DistanceToCam")
    val distanceToCam: Double,
)

@Serializable
data class OreMarker(
    @SerialName("Position")
    var position: Vec3F,
    @SerialName("Text")
    var text: String,
    @SerialName("Distance")
    var distance: Double,
    @SerialName("Materials")
    var materials: List<DefinitionId>,
)

@Serializable
data class GamePlayData(
    @SerialName("OreMarkers")
    val oreMarkers: List<OreMarker>,
)
