package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UseObject(
    @SerialName("Name")
    val name: String,
    @SerialName("SupportedActions")
    val SupportedActions: Int,
    @SerialName("PrimaryAction")
    val primaryAction: Int,
    @SerialName("SecondaryAction")
    val secondaryAction: Int,
    @SerialName("ContinuousUsage")
    val continuousUsage: Boolean,
) {
    companion object {
        val None = 0
        val Manipulate = 1 shl 0
        val OpenTerminal = 1 shl 1
        val OpenInventory = 1 shl 2
        val UseFinished = 1 shl 3           // Finished of using "USE" key (key released)
        val Close =
            1 shl 4// Use object is closing (called before). Ie. character just got out of sight of interactive object
        val PickUp = 1 shl 5
        val BuildPlanner = 1 shl 6
    }
}
