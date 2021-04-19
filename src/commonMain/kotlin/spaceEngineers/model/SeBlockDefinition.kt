package spaceEngineers.model


enum class CubeSize {
    Large, Small;
}

data class SeBlockDefinition(
    //val id: String,
    val blockType: String,
    val buildProgressModels: List<SeBuildProgressModel>,
    val size: Vec3,
    val cubeSize: CubeSize,
)