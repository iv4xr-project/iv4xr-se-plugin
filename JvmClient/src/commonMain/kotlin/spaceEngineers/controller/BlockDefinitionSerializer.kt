package spaceEngineers.controller

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.DataBlockDefinition
import spaceEngineers.model.generatedBlockDefinitionSerializerMappings
import spaceEngineers.util.generator.removeDefinitionPrefix
import kotlin.reflect.KClass

val blockDefinitionMappings = mapOf<String, Map<String, KClass<*>>>(
    "AirtightDoorGenericDefinition" to mapOf(
        "PowerConsumptionIdle" to Float::class,
        "PowerConsumptionMoving" to Float::class,
        "OpeningSpeed" to Float::class,
    ),
    "LCDPanelsBlockDefinition" to mapOf(
        "RequiredPowerInput" to Float::class
    ),
    "PowerProducerDefinition" to mapOf(
        "MaxPowerOutput" to Float::class,
    ),
    "GravityGeneratorDefinition" to mapOf(
        "RequiredPowerInput" to Float::class,
    ),
    "GravityGeneratorBaseDefinition" to mapOf(
        "MinGravityAcceleration" to Float::class,
        "MaxGravityAcceleration" to Float::class,
    )
)


object BlockDefinitionSerializer : JsonContentPolymorphicSerializer<BlockDefinition>(BlockDefinition::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out BlockDefinition> {
        //val id = element.jsonObject["DefinitionId"]!!.jsonObject["Id"]!!.jsonPrimitive.content.removeBuilderPrefix()
        val type = element.jsonObject["Type"]!!.jsonPrimitive.content.removeDefinitionPrefix()
        return generatedBlockDefinitionSerializerMappings[type] ?: DataBlockDefinition.serializer()
    }
}
