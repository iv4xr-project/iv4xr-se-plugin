package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainMenuData(
    @SerialName("Type")
    val type: Int,
) {
    companion object {
        val MAIN = 0
        val IN_GAME = 1
    }
}
