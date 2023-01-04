package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterObservation(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("Velocity")
    override val velocity: Vec3F,
    @SerialName("Gravity")
    val gravity: Vec3F = Vec3F.ZERO,
    @SerialName("Extent")
    val extent: Vec3F,
    @SerialName("JetpackRunning")
    val jetpackRunning: Boolean,
    @SerialName("DampenersOn")
    val dampenersOn: Boolean = false,
    @SerialName("HelmetEnabled")
    val helmetEnabled: Boolean = true,
    @SerialName("LightEnabled")
    val lightEnabled: Boolean = true,
    @SerialName("CurrentLightPower")
    val currentLightPower: Float = 0f,
    @SerialName("Health")
    override val health: Float,
    @SerialName("Oxygen")
    override val oxygen: Float,
    @SerialName("Hydrogen")
    override val hydrogen: Float,
    @SerialName("SuitEnergy")
    override val energy: Float = 1f,
    @SerialName("Camera")
    val camera: BasePose,
    @SerialName("HeadLocalXAngle")
    val headLocalXAngle: Float,
    @SerialName("HeadLocalYAngle")
    val headLocalYAngle: Float,
    @SerialName("TargetBlock")
    val targetBlock: Block?,
    @SerialName("TargetUseObject")
    val targetUseObject: UseObject? = null,
    @SerialName("Movement")
    val movement: CharacterMovement,
    @SerialName("Inventory")
    val inventory: Inventory,
    @SerialName("BootsState")
    val bootsState: BootsState,
    @SerialName("DisplayName")
    override val displayName: String,
    @SerialName("RelativeDampeningEntity")
    val relativeDampeningEntity: BaseEntity? = null,
    @SerialName("MovementFlags")
    val movementFlags: CharacterMovementFlags = CharacterMovementFlags(0u),

    @SerialName("Name")
    override val name: String,
    @SerialName("InScene")
    override val inScene: Boolean,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,

    @SerialName("JetpackControlThrust")
    val jetpackControlThrust: Vec3F = Vec3F.ZERO,
    @SerialName("JetpackFinalThrust")
    val jetpackFinalThrust: Vec3F = Vec3F.ZERO,
    @SerialName("CurrentWeapon")
    val currentWeapon: ExtendedEntity? = null,

) : ExtendedEntity, CharacterStats

interface CharacterStats {
    val health: Float
    val oxygen: Float
    val hydrogen: Float
    val energy: Float
    // TODO: helmet, light, jetpack
}
