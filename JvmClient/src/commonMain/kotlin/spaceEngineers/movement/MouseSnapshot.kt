package spaceEngineers.movement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MouseSnapshot(
    @SerialName("CursorPositionX")
    val cursorPositionX: Int,
    @SerialName("CursorPositionY")
    val cursorPositionY: Int,
    @SerialName("X")
    val x: Int,
    @SerialName("Y")
    val y: Int,
    @SerialName("ScrollWheelValue")
    val scrollWheelValue: Int,

    @SerialName("LeftButton")
    val leftButton: Boolean,
    @SerialName("RightButton")
    val rightButton: Boolean,
    @SerialName("MiddleButton")
    val middleButton: Boolean,
    @SerialName("XButton1")
    val xButton1: Boolean,
    @SerialName("XButton2")
    val xButton2: Boolean,
)
