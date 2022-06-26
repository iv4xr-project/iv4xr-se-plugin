package spaceEngineers.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class BootsState(val value: UInt) {

    fun isWhite(): Boolean {
        return isColour(BootsColour.WHITE)
    }

    fun isYellow(): Boolean {
        return isColour(BootsColour.YELLOW)
    }

    fun isGreen(): Boolean {
        return isColour(BootsColour.GREEN)
    }

    fun toColour(): BootsColour {
        return BootsColour.values().first { value in it.uIntValues }
    }

    fun isColour(bootsColour: BootsColour): Boolean {
        return value in bootsColour.uIntValues
    }

    override fun toString(): String {
        return "${toColour().toString().lowercase()}($value)"
    }

    companion object {
        val INIT = BootsState(0u)
        val DISABLED = BootsState(1u)
        val PROXIMITY = BootsState(2u)
        val ENABLED = BootsState(3u)
    }
}
