package spaceEngineers.model

data class SeBlockDefinition(
    var blockType: String,
    var buildProgressModels: List<SeBuildProgressModel>
)