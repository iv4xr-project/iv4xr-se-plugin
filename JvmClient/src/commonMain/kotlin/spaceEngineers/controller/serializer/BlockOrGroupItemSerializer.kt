package spaceEngineers.controller.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import spaceEngineers.model.BlockGroupItem
import spaceEngineers.model.BlockItem
import spaceEngineers.model.BlockOrGroupItem

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
