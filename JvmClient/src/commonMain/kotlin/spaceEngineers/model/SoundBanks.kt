package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SoundBanks(
    @SerialName("Sound")
    val sound: List<Sound>,
)

@Serializable
data class Sound(
    @SerialName("IsPlaying")
    val isPlaying: Boolean,
    @SerialName("IsPaused")
    val isPaused: Boolean,
    @SerialName("CueEnum")
    val cueEnum: String,
)
