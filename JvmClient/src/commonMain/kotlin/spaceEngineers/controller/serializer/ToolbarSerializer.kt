package spaceEngineers.controller.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import spaceEngineers.model.DataToolbarItem
import spaceEngineers.model.DataToolbarItemActions
import spaceEngineers.model.DataToolbarItemDefinition
import spaceEngineers.model.DataToolbarItemTerminalBlock
import spaceEngineers.model.ToolbarItem

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
