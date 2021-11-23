package spaceEngineers.model

interface DefinitionBase {
    val definitionId: DefinitionId
    val public: Boolean
    val availableInSurvival: Boolean
    val enabled: Boolean
}
