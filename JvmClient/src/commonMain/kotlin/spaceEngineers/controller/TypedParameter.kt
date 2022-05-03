package spaceEngineers.controller

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.KType

data class TypedParameter<T : Any>(
    val name: String,
    val value: T?,
    val type: KClass<T>,
    val ktype: KType,
) {

    fun toJsonElementPair(): Pair<String, JsonElement?> {
        return name to asJsonElement()
    }

    private fun asJsonElement(): JsonElement? {
        return value?.let {
            json.encodeToJsonElement(serializer(ktype), it)
        }
    }
}
