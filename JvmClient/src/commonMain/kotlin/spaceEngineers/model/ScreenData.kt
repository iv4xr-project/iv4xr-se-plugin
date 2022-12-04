package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface ScreenData {
    val isLoaded: Boolean
    val isOpened: Boolean
    val skipTransition: Boolean
    val cancelled: Boolean
    val visible: Boolean
    val closeButtonEnabled: Boolean
}

@Serializable
data class BaseScreenData(
    @SerialName("Name")
    val name: String,
    @SerialName("IsLoaded")
    override val isLoaded: Boolean,
    @SerialName("IsOpened")
    override val isOpened: Boolean,
    @SerialName("SkipTransition")
    override val skipTransition: Boolean,
    @SerialName("Cancelled")
    override val cancelled: Boolean,
    @SerialName("Visible")
    override val visible: Boolean,
    @SerialName("CloseButtonEnabled")
    override val closeButtonEnabled: Boolean,
) : ScreenData {
    val typedName: ScreenName
        get() = name.toScreenName()
}
