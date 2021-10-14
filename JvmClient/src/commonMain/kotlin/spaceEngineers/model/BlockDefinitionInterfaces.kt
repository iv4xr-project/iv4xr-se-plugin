package spaceEngineers.model

// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.

interface AirtightDoorGenericDefinition: BlockDefinition  {
    val powerConsumptionIdle: Float
    val powerConsumptionMoving: Float
    val openingSpeed: Float
}

interface LCDPanelsBlockDefinition: BlockDefinition  {
    val requiredPowerInput: Float
}

interface PowerProducerDefinition: BlockDefinition  {
    val maxPowerOutput: Float
}

interface GravityGeneratorDefinition: GravityGeneratorBaseDefinition  {
    val requiredPowerInput: Float
}

interface GravityGeneratorBaseDefinition: BlockDefinition  {
    val minGravityAcceleration: Float
    val maxGravityAcceleration: Float
}

