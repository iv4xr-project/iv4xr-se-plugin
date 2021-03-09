package spaceEngineers.commands

import kotlin.jvm.JvmOverloads

class InteractionArgs @JvmOverloads constructor(
    val interactionType: Int,
    val slot: Int = -1,
    val page: Int = -1,
    val allowSizeChange: Boolean = false
) {
    constructor(
        interactionType: InteractionType,
        slot: Int = -1,
        page: Int = -1,
        allowSizeChange: Boolean = false
    ) : this(interactionType.value, slot, page, allowSizeChange)
}