package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BlockDefinition(
    @SerialName("DefinitionId")
    val definitionId: DefinitionId,
    /**
     * List containing integrity thresholds that change block state (and visual model).
     */
    @SerialName("BuildProgressModels")
    val buildProgressModels: List<BuildProgressModel>,
    /**
     * Size of the block in integers. Cube block would have size 1x1x1. This size is not in game units. To convert to game units either multiply by 0.5 for small or 2.5 for large block.
     * @see cubeSize to determine small/large
     */
    @SerialName("Size")
    val size: Vec3,
    @SerialName("CubeSize")
    val cubeSize: CubeSize,
    @SerialName("MountPoints")
    val mountPoints: List<MountPoint>,
    @SerialName("Public")
    val public: Boolean,
    @SerialName("AvailableInSurvival")
    val availableInSurvival: Boolean,
    @SerialName("Enabled")
    val enabled: Boolean,
)
