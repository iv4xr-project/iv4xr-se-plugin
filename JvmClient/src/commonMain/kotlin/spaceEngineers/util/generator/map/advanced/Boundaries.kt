package spaceEngineers.util.generator.map.advanced

import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.allBetween

data class Boundaries(val min: Vec3I, val max: Vec3I) {

    init {
        require(min.x <= max.x)
        require(min.y <= max.y)
        require(min.z <= max.z)
    }

    fun allBetween(): Sequence<Vec3I> {
        return min.allBetween(max)
    }

    fun toFloor(y: Int): Boundaries {
        return Boundaries(min.copy(y = y), max.copy(y = y))
    }
}
