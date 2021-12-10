package spaceEngineers.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class CharacterMovement(val value: UShort) {

    val mode: UShort
        get() = value and movementTypeMask.toUShort()

    val direction: UShort
        get() = value and movementDirectionMask.toUShort()


    val speed: UShort
        get() = value and movementSpeedMask.toUShort()

    val rotation: UShort
        get() = value and rotationMask.toUShort()

    companion object {
        const val movementTypeMask      = 0x000f // 4 bits (0 - 3) for movement type should be enough even for the future
        const val movementDirectionMask = 0x03f0 // 6 bits (4 - 9)
        const val movementSpeedMask     = 0x0c00 // 2 bits (10 - 11)
        const val rotationMask          = 0x3000 // 2 bits (12 - 13)

        // The movement types are mutually exclusive - i.e. you cannot be sitting and crouching at the same time
        const val standing = 0
        const val sitting = 1
        const val crouching = 2
        const val flying = 3
        const val falling = 4
        const val jump = 5
        const val died = 6
        const val ladder = 7

        // Movement direction
        const val noDirection = 0
        const val forward = 1 shl 4
        const val backward = 1 shl 5
        const val left = 1 shl 6
        const val right = 1 shl 7
        const val up = 1 shl 8
        const val down = 1 shl 9

        // Movement speed
        const val normalSpeed = 0
        const val fast = 1 shl 10
        const val veryFast = 1 shl 11

        // Rotation
        const val notRotating = 0
        const val rotatingLeft = 1 shl 12
        const val rotatingRight = 1 shl 13

        //Ladder
        const val ladderOut = 1 shl 14

    }
}
