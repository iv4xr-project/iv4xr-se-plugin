package spaceEngineers.controller

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import spaceEngineers.model.*
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.jsonrpc.JsonRpcResponse
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf


val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    allowSpecialFloatingPointValues = true
    serializersModule = SerializersModule {
        polymorphic(Block::class) {
            default { BlockSerializer }
        }
        polymorphic(BlockDefinition::class) {
            default { BlockDefinitionSerializer }
        }
        polymorphic(DefinitionBase::class) {
            default { DataDefinitionBase.serializer() }
        }
        polymorphic(PhysicalItemDefinition::class) {
            default { DataPhysicalItemDefinition.serializer() }
        }
        polymorphic(BlockOrGroupItem::class) {
            default { BlockOrGroupItemSerializer }
        }
        polymorphic(ExtendedEntity::class) {
            default { EntitySerializer }
        }
        polymorphic(ToolbarItem::class) {
            default { ToolbarSerializer }
        }
    }
}

object ToolbarSerializer : JsonContentPolymorphicSerializer<ToolbarItem>(ToolbarItem::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ToolbarItem> {
        return if ("Id" in element.jsonObject) {
            DataToolbarItemDefinition.serializer()
        } else if ("BlockEntityId" in element.jsonObject) {
            DataToolbarItemTerminalBlock.serializer()
        } else if ("ActionId" in element.jsonObject) {
            DataToolbarItemActions.serializer()
        } else {
            DataToolbarItem.serializer()
        }
    }

}
