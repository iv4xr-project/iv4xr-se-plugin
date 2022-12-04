package spaceEngineers.controller

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import spaceEngineers.model.Block
import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.BlockOrGroupItem
import spaceEngineers.model.DataDefinitionBase
import spaceEngineers.model.DataPhysicalItemDefinition
import spaceEngineers.model.DataToolbarItem
import spaceEngineers.model.DataToolbarItemActions
import spaceEngineers.model.DataToolbarItemDefinition
import spaceEngineers.model.DataToolbarItemTerminalBlock
import spaceEngineers.model.DefinitionBase
import spaceEngineers.model.ExtendedEntity
import spaceEngineers.model.PhysicalItemDefinition
import spaceEngineers.model.ToolbarItem

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
