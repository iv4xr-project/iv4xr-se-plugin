package spaceEngineers.model

interface AirtightDoorGenericDefinition: BlockDefinition {
    val powerConsumptionIdle: Float
    val powerConsumptionMoving: Float
    val openingSpeed: Float
}
