package spaceEngineers.controller

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import spaceEngineers.controller.serializer.BlockOrGroupItemSerializer
import spaceEngineers.controller.serializer.BlockSerializer
import spaceEngineers.controller.serializer.EntitySerializer
import spaceEngineers.controller.serializer.ToolbarSerializer
import spaceEngineers.model.Block
import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.BlockOrGroupItem
import spaceEngineers.model.DataDefinitionBase
import spaceEngineers.model.DataPhysicalItemDefinition
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
