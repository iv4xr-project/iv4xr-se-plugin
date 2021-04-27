package spaceEngineers.model


data class SeBlockDefinition(
    val id: String,
    val blockType: String,
    val buildProgressModels: List<SeBuildProgressModel>,
    val size: Vec3,
    val cubeSize: CubeSize,
    val mountPoints: List<MountPoint>,
    val public: Boolean,
    val availableInSurvival: Boolean,
    val enabled: Boolean,
)
