package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterObservation(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("Velocity")
    val velocity: Vec3,
    @SerialName("Extent")
    val extent: Vec3,
    @SerialName("JetpackRunning")
    val jetpackRunning: Boolean,
    @SerialName("HelmetEnabled")
    val helmetEnabled: Boolean = true,
    @SerialName("HealthRatio")
    val healthRatio: Float = 1f,
    @SerialName("Camera")
    val camera: BasePose,
    @SerialName("HeadLocalXAngle")
    val headLocalXAngle: Float,
    @SerialName("HeadLocalYAngle")
    val headLocalYAngle: Float,
    @SerialName("TargetBlock")
    val targetBlock: Block?,
) : Entity
