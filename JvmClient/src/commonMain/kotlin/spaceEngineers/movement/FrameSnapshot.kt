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
        fun fromDirection(compositeDirection3d: CompositeDirection3d, movementType: CharacterMovementType): FrameSnapshot {
            return FrameSnapshot(
                input = InputSnapshot(keyboard = compositeDirection3d.toKeyboardSnapshot(movementType))
            )
        }
    }
}
