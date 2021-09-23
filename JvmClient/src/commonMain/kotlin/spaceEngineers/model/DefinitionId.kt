package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DefinitionId(
    @SerialName("Id")
    val id: String,
    @SerialName("Type")
    val type: String,
) {

    override fun toString(): String {
        return "$id/$type"
    }

    companion object {
        const val ID_PREFIX = "MyObjectBuilder_"
        const val CUBE_BLOCK = "CubeBlock"

        fun cubeBlock(type: String): DefinitionId {
            return create(CUBE_BLOCK, type)
        }

        fun create(id: String, type: String): DefinitionId {
            return DefinitionId("$ID_PREFIX$id", type)
        }
    }
}
