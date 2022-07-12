package spaceEngineers.movement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InputSnapshot(
    @SerialName("Keyboard")
    val keyboard: KeyboardSnapshot? = null,
    @SerialName("Mouse")
    val mouse: MouseSnapshot? = null,
)
