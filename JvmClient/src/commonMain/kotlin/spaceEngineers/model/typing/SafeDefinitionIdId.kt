package spaceEngineers.model.typing

import spaceEngineers.model.DefinitionId

open class SafeDefinitionIdId {
    override fun toString(): String {
        return this::class.simpleName ?: error("No simple name!")
    }
}

@JvmInline
value class DefinitionIdType(val value: String) {
    override fun toString(): String {
        return value
    }
}

fun create(id: SafeDefinitionIdId, type: DefinitionIdType): DefinitionId {
    return DefinitionId.create(id.toString(), type.value)
}
