package spaceEngineers.movement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class MouseButton {
    LEFT, RIGHT, MIDDLE, XBUTTON1, XBUTTON2;
}

@Serializable
data class MouseSnapshot(
    @SerialName("CursorPositionX")
    val cursorPositionX: Int = 0,
    @SerialName("CursorPositionY")
    val cursorPositionY: Int = 0,
    @SerialName("X")
    val x: Int = 0,
    @SerialName("Y")
    val y: Int = 0,
    @SerialName("ScrollWheelValue")
    val scrollWheelValue: Int = 0,

    @SerialName("LeftButton")
    val leftButton: Boolean = false,
    @SerialName("RightButton")
    val rightButton: Boolean = false,
    @SerialName("MiddleButton")
    val middleButton: Boolean = false,
    @SerialName("XButton1")
    val xButton1: Boolean = false,
    @SerialName("XButton2")
    val xButton2: Boolean = false,
) {
    companion object {
        fun buttonClicked(mouseButton: MouseButton): MouseSnapshot {
            return when (mouseButton) {
                MouseButton.LEFT -> MouseSnapshot(leftButton = true)
                MouseButton.RIGHT -> MouseSnapshot(rightButton = true)
                MouseButton.MIDDLE -> MouseSnapshot(middleButton = true)
                MouseButton.XBUTTON1 -> MouseSnapshot(xButton1 = true)
                MouseButton.XBUTTON2 -> MouseSnapshot(xButton2 = true)
                else -> error("no snapshot for button $mouseButton")
            }
        }

        fun nothingClicked(): MouseSnapshot {
            return MouseSnapshot()
        }
    }
}
