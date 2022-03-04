package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class File(
    @SerialName("Name")
    val name: String,
    @SerialName("FullName")
    val fullName: String,
    @SerialName("IsDirectory")
    val isDirectory: Boolean,
)

@Serializable
data class LoadGameData(
    @SerialName("CurrentDirectory")
    val currentDirectory: File,
    @SerialName("RootDirectory")
    val rootDirectory: File,
    @SerialName("Files")
    val files: List<File>,
)
