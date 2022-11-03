package spaceEngineers.controller

import spaceEngineers.controller.useobject.UseObjectExtensions
import spaceEngineers.model.BlockId
import spaceEngineers.model.Vec3F
import spaceEngineers.movement.ReplayMovement
import spaceEngineers.movement.VectorMovement
import spaceEngineers.navigation.CharacterNavigation
import spaceEngineers.navigation.PathFinder
import spaceEngineers.navigation.ScreenNavigation


data class CharacterExtensions(
    val navigation: CharacterNavigation,
    val replayMovement: ReplayMovement,
    val vectorMovement: VectorMovement,
) {
    constructor(spaceEngineers: SpaceEngineers, pathFinder: PathFinder<BlockId, Vec3F, String, String>) : this(
        navigation = CharacterNavigation(spaceEngineers, pathFinder = pathFinder),
        vectorMovement = VectorMovement(spaceEngineers),
        replayMovement = ReplayMovement(spaceEngineers),
    )
}

data class BlockExtensions(
    val useObject: UseObjectExtensions,
)

data class ScreenExtensions(
    val navigation: ScreenNavigation,
) {
    constructor(spaceEngineers: SpaceEngineers) : this(
        navigation = ScreenNavigation(spaceEngineers),
    )
}

data class SpaceEngineersExtensions(
    val character: CharacterExtensions,
    val screen: ScreenExtensions,
    val blocks: BlockExtensions,
) {

    constructor(spaceEngineers: SpaceEngineers, pathFinder: PathFinder<BlockId, Vec3F, String, String>) : this(
        character = CharacterExtensions(spaceEngineers, pathFinder = pathFinder),
        screen = ScreenExtensions(spaceEngineers),
        blocks = BlockExtensions(useObject = UseObjectExtensions(spaceEngineers)),
    )

}

interface ExtendedSpaceEngineers : SpaceEngineers {
    val extensions: SpaceEngineersExtensions
}

class DataExtendedSpaceEngineers(
    val spaceEngineers: SpaceEngineers,
    pathFinder: PathFinder<BlockId, Vec3F, String, String>,
    override val extensions: SpaceEngineersExtensions = SpaceEngineersExtensions(spaceEngineers, pathFinder)
) : SpaceEngineers by spaceEngineers, ExtendedSpaceEngineers {

}
