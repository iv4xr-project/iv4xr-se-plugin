package spaceEngineers.movement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeyboardSnapshot(
    @SerialName("PressedKeys")
    val pressedKeys: List<Int>,
    @SerialName("Text")
    val text: List<Char>,
)
