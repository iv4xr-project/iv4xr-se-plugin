package spaceEngineers.controller

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import spaceEngineers.model.*
import spaceEngineers.model.DefinitionId.Companion.ANGLE_GRINDER
import spaceEngineers.model.DefinitionId.Companion.CUBE_GRID
import spaceEngineers.model.DefinitionId.Companion.HAND_DRILL
import spaceEngineers.model.DefinitionId.Companion.PHYSICAL_GUN
import spaceEngineers.model.DefinitionId.Companion.WELDER
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

fun getDefinitionIdId(element: JsonElement): String? {
    return element.jsonObject["DefinitionId"]?.jsonObject?.get("Id")?.jsonPrimitive?.content?.removeBuilderPrefix()
}

object BlockSerializer : JsonContentPolymorphicSerializer<Block>(Block::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Block> {
        val id = getDefinitionIdId(element)
        return generatedSerializerMappings[id] ?: DataBlock.serializer()
    }
}

object EntitySerializer : JsonContentPolymorphicSerializer<ExtendedEntity>(ExtendedEntity::class) {

    private val generatedSerializerMappings = mutableMapOf(
        PHYSICAL_GUN to HandTool.serializer(),
        CUBE_GRID to CubeGrid.serializer(),
        WELDER to HandTool.serializer(),
        ANGLE_GRINDER to HandTool.serializer(),
        HAND_DRILL to HandTool.serializer(),
    )


    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ExtendedEntity> {
        val id = getDefinitionIdId(element)
        return generatedSerializerMappings[id] ?: BaseEntity.serializer()
    }
}


object BlockOrGroupItemSerializer : JsonContentPolymorphicSerializer<BlockOrGroupItem>(BlockOrGroupItem::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out BlockOrGroupItem> {
        if (element.jsonObject.containsKey("Name")) {
            return BlockGroupItem.serializer()
        } else if (element.jsonObject.containsKey("Block")) {
            return BlockItem.serializer()
        }
        error("Cannot find serializer for $element")
    }
}
