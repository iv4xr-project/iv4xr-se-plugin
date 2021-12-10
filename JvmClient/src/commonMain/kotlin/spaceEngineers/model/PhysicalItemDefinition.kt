package spaceEngineers.model

interface PhysicalItemDefinition: DefinitionBase {
    val size: Vec3F
    val mass: Float
    val volume: Float
    val health: Int
}
