package spaceEngineers.model


data class BlockDefinition(
    val id: String,
    val blockType: String,
    /**
     * List containing integrity thresholds that change block state (and visual model).
     */
    val buildProgressModels: List<BuildProgressModel>,
    /**
     * Size of the block in integers. Cube block would have size 1x1x1. This size is not in game units. To convert to game units either multiply by 0.5 for small or 2.5 for large block.
     * @see cubeSize to determine small/large
     */
    val size: Vec3,
    val cubeSize: CubeSize,
    val mountPoints: List<MountPoint>,
    val public: Boolean,
    val availableInSurvival: Boolean,
    val enabled: Boolean,
)
