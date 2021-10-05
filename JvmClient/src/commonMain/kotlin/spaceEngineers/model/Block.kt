package spaceEngineers.model


interface Block : Entity {
    val maxIntegrity: Float
    val buildIntegrity: Float
    val integrity: Float
    val minPosition: Vec3F
    val maxPosition: Vec3F
    val size: Vec3F
    val useObjects: List<UseObject>
    val functional: Boolean
    val working: Boolean
    val definitionId: DefinitionId
}
