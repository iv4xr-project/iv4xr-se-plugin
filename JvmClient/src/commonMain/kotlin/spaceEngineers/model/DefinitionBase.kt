package spaceEngineers.model

data class DefinitionBase(
    val id: String,
    val blockType: String,
    val public: Boolean,
    val availableInSurvival: Boolean,
    val enabled: Boolean,
)
