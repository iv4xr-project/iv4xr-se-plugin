package spaceEngineers.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

enum class GameModeEnum {
    CREATIVE, SURVIVAL;
}

@Serializable
@JvmInline
value class GameMode(val value: UShort) {

    override fun toString(): String {
        return toEnum().toString()
    }

    fun toEnum(): GameModeEnum {
        return when (value) {
            CREATIVE.value -> GameModeEnum.CREATIVE
            SURVIVAL.value -> GameModeEnum.SURVIVAL
            else -> error("No enum for mode $value")
        }
    }

    companion object {
        val CREATIVE = GameMode(0u)
        val SURVIVAL = GameMode(1u)
    }
}
