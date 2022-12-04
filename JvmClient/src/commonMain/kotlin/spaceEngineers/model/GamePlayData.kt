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

interface HudStatsWrapper {
    val dampenersOn: Boolean
    val relativeDampenersOn: Boolean
    val jetpackOn: Boolean
    val speed: Float
    val artificialGravity: Float
    val naturalGravity: Float
    val helmet: Boolean
}

@Serializable
data class Hud(
    @SerialName("Stats")
    val stats: Map<String, Float>,
) {
    val statsWrapper: HudStatsWrapper
        get() = object : HudStatsWrapper {
            override val dampenersOn: Boolean = (stats["ControlledEntityDampeners"] ?: 0f) > 0f
            override val relativeDampenersOn: Boolean = stats["ControlledEntityDampeners"] == 0.5f
            override val jetpackOn: Boolean = stats["PlayerJetpack"] == 1.0f
            override val helmet: Boolean = stats["PlayerHelmet"] == 1.0f
            override val speed: Float = stats["ControlledEntitySpeed"] ?: error("no speed in stats")
            override val artificialGravity: Float = stats["ArtificialGravity"] ?: error("no artificialGravity in stats")
            override val naturalGravity: Float = stats["NaturalGravity"] ?: error("no naturalGravity in stats")
        }
}

@Serializable
data class GamePlayData(
    @SerialName("OreMarkers")
    val oreMarkers: List<OreMarker>,
    @SerialName("Hud")
    val hud: Hud,
)
