package spaceEngineers.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class CharacterMovementFlags(val value: UShort) {

    val wantsWalk: Boolean
        get() = value and Walk.toUShort() > 0u

    companion object {
        val Jump = 1
        val Sprint = 2
        val FlyUp = 4
        val FlyDown = 8
        val Crouch = 16
        val Walk = 32
    }
}
