package spaceEngineers.model

import kotlinx.serialization.SerialName


interface Entity: Pose {
    // TODO(PP): Add entity type
    @SerialName("Id")
    val id: String
}
