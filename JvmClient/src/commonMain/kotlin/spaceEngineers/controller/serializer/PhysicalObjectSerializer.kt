package spaceEngineers.controller.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import spaceEngineers.model.DataPhysicalObject
import spaceEngineers.model.GasContainerObject
import spaceEngineers.model.PhysicalObject
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.util.generator.removeBuilderPrefix

object PhysicalObjectSerializer : JsonContentPolymorphicSerializer<PhysicalObject>(PhysicalObject::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out PhysicalObject> {
        val id = getDefinitionIdId(element, "Id")
        return if (id == DefinitionIds.GasContainerObject.HydrogenBottle.id.removeBuilderPrefix()) {
            GasContainerObject.serializer()
        } else {
            DataPhysicalObject.serializer()
        }
    }
}
