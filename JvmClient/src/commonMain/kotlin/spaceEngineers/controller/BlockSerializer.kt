package spaceEngineers.controller

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import spaceEngineers.model.*
import spaceEngineers.util.generator.removeBuilderPrefix
import kotlin.reflect.KClass

val blockMappings = mapOf<String, Map<String, KClass<*>>>(
    "TerminalBlock" to mapOf(
        "ShowInInventory" to Boolean::class,
        "ShowInTerminal" to Boolean::class,
        "ShowOnHUD" to Boolean::class,
    ),
    "FunctionalBlock" to mapOf(
        "Enabled" to Boolean::class
    ),
    "DoorBase" to mapOf(
        "Open" to Boolean::class,
        "AnyoneCanUse" to Boolean::class
    ),
    "FueledPowerProducer" to mapOf(
        "MaxOutput" to Float::class,
        "CurrentOutput" to Float::class,
        "Capacity" to Float::class,
    )
)

val serializerMapping = mutableMapOf<String, DeserializationStrategy<out Block>>()

object BlockSerializer : JsonContentPolymorphicSerializer<Block>(Block::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Block> {
        val id = element.jsonObject["DefinitionId"]!!.jsonObject["Id"]!!.jsonPrimitive.content.removeBuilderPrefix()
        return generatedSerializerMappings[id] ?: DataBlock.serializer()
    }
}
