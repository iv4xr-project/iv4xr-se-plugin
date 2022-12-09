package spaceEngineers.controller.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import spaceEngineers.model.Block
import spaceEngineers.model.DataBlock
import spaceEngineers.model.generatedSerializerMappings

object BlockSerializer : JsonContentPolymorphicSerializer<Block>(Block::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Block> {
        val id = getDefinitionIdId(element)
        return generatedSerializerMappings[id] ?: DataBlock.serializer()
    }
}
