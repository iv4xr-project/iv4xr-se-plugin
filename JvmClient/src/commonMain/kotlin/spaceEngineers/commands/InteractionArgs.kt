package spaceEngineers.commands

import spaceEngineers.model.ToolbarLocation
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
        toolbarLocation: ToolbarLocation,
        allowSizeChange: Boolean = false
    ) : this(interactionType.value, toolbarLocation.slot, toolbarLocation.page, allowSizeChange = allowSizeChange)

    constructor(
        interactionType: InteractionType,
        slot: Int = -1,
        page: Int = -1,
        itemName: String
    ) : this(interactionType.value, slot, page, false, itemName)


    companion object {
        fun equip(toolbarLocation: ToolbarLocation): InteractionArgs {
            return InteractionArgs(InteractionType.EQUIP, toolbarLocation)
        }
    }
}