package spaceEngineers.util.generator.map.advanced

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.util.generator.map.Orientations

data class MutableCell(
    var id: DefinitionId,
    var orientations: List<Orientations> = listOf(Orientations()),
    var color: Vec3F? = null,
    /* lower means it is gonna build sooner */
    var priority: Int = 0,
    var customName: String? = null
) {

    companion object {
        val WALL = MutableCell(
            id = DefinitionIds.CubeBlock.LargeHeavyBlockArmorBlock,
            orientations = listOf(Orientations()),
            color = null,
        )
    }
}
