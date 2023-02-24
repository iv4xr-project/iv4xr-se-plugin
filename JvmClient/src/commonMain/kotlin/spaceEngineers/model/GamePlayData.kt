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
    val position: Vec3F,
    @SerialName("Text")
    val text: String,
    @SerialName("Distance")
    val distance: Double,
    @SerialName("Materials")
    val materials: List<DefinitionId>,
)

interface HudStatsWrapper : CharacterStats {
    val dampenersOn: Boolean
    val relativeDampenersOn: Boolean
    val jetpackOn: Boolean
    val speed: Float
    val artificialGravity: Float
    val naturalGravity: Float
    val helmet: Boolean
}

@Serializable
data class HudNotification(
    @SerialName("Text")
    val text: String,
)

@Serializable
data class Hud(
    @SerialName("Stats")
    val stats: Map<String, Float>,
    @SerialName("Notifications")
    val notifications: List<HudNotification> = emptyList(),
) {
    val statsWrapper: HudStatsWrapper
        get() = object : HudStatsWrapper {
            override val dampenersOn: Boolean = (stats["ControlledEntityDampeners"] ?: 0f) > 0f
            override val relativeDampenersOn: Boolean = stats["ControlledEntityDampeners"] == 0.5f
            override val jetpackOn: Boolean = stats["PlayerJetpack"] == 1.0f
            override val helmet: Boolean = stats["PlayerHelmet"] == 1.0f
            override val health: Float = stats["PlayerHealth"] ?: error("no health in stats")
            override val oxygen: Float = stats["PlayerOxygen"] ?: error("no oxygen in stats")
            override val energy: Float = stats["PlayerEnergy"] ?: error("no energy in stats")
            override val hydrogen: Float = stats["PlayerHydrogen"] ?: error("no hydrogen in stats")
            override val speed: Float = stats["ControlledEntitySpeed"] ?: error("no speed in stats")
            override val artificialGravity: Float = stats["ArtificialGravity"] ?: error("no artificialGravity in stats")
            override val naturalGravity: Float = stats["NaturalGravity"] ?: error("no naturalGravity in stats")
        }
}

@Serializable
data class LocationMarker(
    @SerialName("Position")
    val position: Vec3F,
    @SerialName("Text")
    val text: String,
)

@Serializable
data class GamePlayData(
    @SerialName("OreMarkers")
    val oreMarkers: List<OreMarker>,
    @SerialName("Hud")
    val hud: Hud,
    @SerialName("LocationMarkers")
    val locationMarkers: List<LocationMarker> = emptyList(),
)
