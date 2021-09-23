package spaceEngineers.model


interface Block : Entity {
    val maxIntegrity: Float
    val buildIntegrity: Float
    val integrity: Float
    val minPosition: Vec3
    val maxPosition: Vec3
    val size: Vec3
    val useObjects: List<UseObject>
    val functional: Boolean
    val working: Boolean
    val definitionId: DefinitionId
}
