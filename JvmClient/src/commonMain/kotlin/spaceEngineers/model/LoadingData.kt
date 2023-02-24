package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoadingData(
    @SerialName("ScreenToLoad")
    val screenToLoad: String,
    @SerialName("ScreenToUnload")
    val screenToUnload: String,
    @SerialName("CurrentText")
    val currentText: String,
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
) : ScreenData
