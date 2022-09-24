package spaceEngineers.controller

import spaceEngineers.movement.ReplayMovement
import spaceEngineers.movement.VectorMovement
import spaceEngineers.navigation.CharacterNavigation
import spaceEngineers.navigation.ScreenNavigation


data class CharacterExtensions(
    val navigation: CharacterNavigation,
    val replayMovement: ReplayMovement,
    val vectorMovement: VectorMovement,
) {
    constructor(spaceEngineers: SpaceEngineers) : this(
        navigation = CharacterNavigation(spaceEngineers),
        vectorMovement = VectorMovement(spaceEngineers),
        replayMovement = ReplayMovement(spaceEngineers),
    )
}

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
) {

    constructor(spaceEngineers: SpaceEngineers) : this(
        character = CharacterExtensions(spaceEngineers),
        screen = ScreenExtensions(spaceEngineers),
    )

}

interface ExtendedSpaceEngineers: SpaceEngineers {
    val extensions: SpaceEngineersExtensions
}

class DataExtendedSpaceEngineers(
    private val spaceEngineers: SpaceEngineers,
    override val extensions: SpaceEngineersExtensions = SpaceEngineersExtensions(spaceEngineers)
) : SpaceEngineers by spaceEngineers, ExtendedSpaceEngineers {

}
