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

@Serializable
data class ParticleEffect(
    @SerialName("Name")
    val name: String,
    @SerialName("Position")
    val position: Vec3F,
)

@Serializable
data class Particles(
    @SerialName("Enabled")
    val enabled: Boolean,
    @SerialName("Paused")
    val paused: Boolean,
    @SerialName("Effects")
    val effects: List<ParticleEffect>,
)
