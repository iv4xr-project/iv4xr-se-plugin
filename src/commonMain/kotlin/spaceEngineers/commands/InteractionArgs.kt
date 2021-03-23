package spaceEngineers.commands

import kotlin.jvm.JvmOverloads

class InteractionArgs @JvmOverloads constructor(
    val interactionType: Int,
    val slot: Int = -1,
    val page: Int = -1,
    val allowSizeChange: Boolean = false,
    val itemName: String? = null
) {
    constructor(
        interactionType: InteractionType,
        slot: Int = -1,
        page: Int = -1,
        allowSizeChange: Boolean = false
    ) : this(interactionType.value, slot, page, allowSizeChange)

    constructor(
       interactionType: InteractionType,
       slot: Int = -1,
       page: Int = -1,
       itemName: String
    ) : this(interactionType.value, slot, page, false, itemName)
}