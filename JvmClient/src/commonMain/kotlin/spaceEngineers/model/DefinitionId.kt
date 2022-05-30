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
        const val PHYSICAL_GUN = "PhysicalGunObject"
        const val ORE = "Ore"
        const val INGOT = "Ingot"
        const val DOOR = "AirtightSlideDoor"
        const val REACTOR = "Reactor"

        fun door(type: String): DefinitionId {
            return create(DOOR, type)
        }

        fun reactor(type: String): DefinitionId {
            return create(REACTOR, type)
        }

        fun cubeBlock(type: String): DefinitionId {
            return create(CUBE_BLOCK, type)
        }

        fun physicalGun(type: String): DefinitionId {
            return create(PHYSICAL_GUN, type)
        }

        fun parse(definition: String): DefinitionId {
            if (!definition.contains("/")) {
                return cubeBlock(definition)
            }
            val parts = definition.split("/")
            check(parts.size == 2)
            return create(parts.first(), parts.last())
        }

        fun ore(type: String): DefinitionId {
            return create(ORE, type)
        }

        fun ingot(type: String): DefinitionId {
            return create(INGOT, type)
        }

        fun create(id: String, type: String): DefinitionId {
            if (id.startsWith(ID_PREFIX)) {
                return DefinitionId(id, type)
            }
            return DefinitionId("$ID_PREFIX$id", type)
        }
    }
}
