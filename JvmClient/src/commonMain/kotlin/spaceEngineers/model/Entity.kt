package spaceEngineers.model

import kotlinx.serialization.SerialName

typealias BlockId = String

interface Entity: Pose {
    @SerialName("Id")
    val id: String
}
