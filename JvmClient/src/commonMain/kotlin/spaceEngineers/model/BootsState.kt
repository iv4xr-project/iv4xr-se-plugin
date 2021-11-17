package spaceEngineers.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class BootsState(val value: UInt) {

    fun isWhite(): Boolean {
        return this.value in setOf(INIT, DISABLED).map { it.value }
    }

    fun isYellow(): Boolean {
        return value == PROXIMITY.value
    }

    fun isGreen(): Boolean {
        return value == ENABLED.value
    }

    companion object {
        val INIT = BootsState(0u)
        val DISABLED = BootsState(1u)
        val PROXIMITY = BootsState(2u)
        val ENABLED = BootsState(3u)
    }
}
