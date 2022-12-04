package spaceEngineers.movement

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.extensions.normalizeAsMovement

interface CharacterMovement {
    fun move(
        direction3d: CompositeDirection3d,
        movementType: CharacterMovementType = CharacterMovementType.RUN,
        ticks: Int = 1
    )

    fun rotate(rotationDirection: RotationDirection, ticks: Int = 1)
}

class ReplayMovement(val spaceEngineers: SpaceEngineers) : CharacterMovement {

    override fun move(direction3d: CompositeDirection3d, movementType: CharacterMovementType, ticks: Int) =
        with(spaceEngineers) {
            if (movementType.requiresWalkCheck) {
                val wantsWalk = observer.observe().movementFlags.wantsWalk
                if (wantsWalk == (movementType == CharacterMovementType.RUN)) {
                    character.switchWalk()
                }
            }
            val frame = FrameSnapshot.fromDirection(direction3d, movementType)
            this.input.startPlaying(
                List(ticks) { frame }
            )
        }

    override fun rotate(rotationDirection: RotationDirection, ticks: Int) =
        with(spaceEngineers) {
            val frame = FrameSnapshot.fromRotationDirection(rotationDirection)
            this.input.startPlaying(
                List(ticks) { frame }
            )
        }
}

class VectorMovement(
    val spaceEngineers: SpaceEngineers,
    private val vectorMultiplier: Float = 9f,
) : CharacterMovement {

    override fun move(direction3d: CompositeDirection3d, movementType: CharacterMovementType, ticks: Int) =
        with(spaceEngineers) {
            character.moveAndRotate(
                movement = direction3d.vector.normalizeAsMovement(characterMovementType = movementType),
                ticks = ticks
            )
            Unit
        }

    override fun rotate(rotationDirection: RotationDirection, ticks: Int) =
        with(spaceEngineers) {
            character.moveAndRotate(
                rotation3 = rotationDirection.vector * vectorMultiplier,
                ticks = ticks
            )
            Unit
        }
}
