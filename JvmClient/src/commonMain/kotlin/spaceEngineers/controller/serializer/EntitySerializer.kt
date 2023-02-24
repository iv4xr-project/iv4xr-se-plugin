package spaceEngineers.controller.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import spaceEngineers.model.BaseEntity
import spaceEngineers.model.CubeGrid
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.ExtendedEntity
import spaceEngineers.model.HandTool

object EntitySerializer : JsonContentPolymorphicSerializer<ExtendedEntity>(ExtendedEntity::class) {

    private val generatedSerializerMappings = mutableMapOf(
        DefinitionId.PHYSICAL_GUN to HandTool.serializer(),
        DefinitionId.CUBE_GRID to CubeGrid.serializer(),
        DefinitionId.WELDER to HandTool.serializer(),
        DefinitionId.ANGLE_GRINDER to HandTool.serializer(),
        DefinitionId.HAND_DRILL to HandTool.serializer(),
    )

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ExtendedEntity> {
        val id = getDefinitionIdId(element)
        return generatedSerializerMappings[id] ?: BaseEntity.serializer()
    }
}
