package spaceEngineers.movement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import spaceEngineers.model.CharacterMovementType

@Serializable
data class FrameSnapshot(
    @SerialName("Input")
    val input: InputSnapshot,
) {
    companion object {
        fun fromDirection(
            compositeDirection3d: CompositeDirection3d,
            movementType: CharacterMovementType
        ): FrameSnapshot {
            return FrameSnapshot(
                input = InputSnapshot(keyboard = compositeDirection3d.toKeyboardSnapshot(movementType))
            )
        }

        fun fromRotationDirection(rotationDirection: RotationDirection): FrameSnapshot {
            return FrameSnapshot(
                input = InputSnapshot(keyboard = rotationDirection.toKeyboardSnapshot())
            )
        }

        fun clicks(mouseButton: MouseButton = MouseButton.LEFT, clickCount: Int = 1): List<FrameSnapshot> {
            val mousePressed = FrameSnapshot(
                InputSnapshot(mouse = MouseSnapshot.buttonClicked(mouseButton))
            )
            val mouseNotPressed = FrameSnapshot(
                InputSnapshot(mouse = MouseSnapshot.nothingClicked())
            )
            return (0 until clickCount).flatMap {
                listOf(mouseNotPressed, mousePressed, mouseNotPressed)
            }
        }

        fun shoots(mouseButton: MouseButton = MouseButton.LEFT, clickCount: Int = 5): List<FrameSnapshot> {
            val mousePressed = FrameSnapshot(
                InputSnapshot(
                    mouse = MouseSnapshot.buttonClicked(mouseButton),
                    keyboard = KeyboardSnapshot(
                        pressedKeys = listOf(1, 183),
                    )
                ),
            )
            return (0 until clickCount).flatMap {
                listOf(mousePressed)
            }
        }

        fun empty(): List<FrameSnapshot> {
            return listOf(
                FrameSnapshot(
                    InputSnapshot(keyboard = KeyboardSnapshot(), mouse = MouseSnapshot.nothingClicked())
                )
            )
        }
    }
}
