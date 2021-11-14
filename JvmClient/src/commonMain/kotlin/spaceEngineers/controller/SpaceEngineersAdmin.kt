package spaceEngineers.controller

import spaceEngineers.model.*

interface SpaceEngineersAdmin {
    val blocks: BlocksAdmin
    val character: CharacterAdmin
    fun setFrameLimitEnabled(enabled: Boolean)
}

interface BlocksAdmin {
    fun placeAt(
        blockDefinitionId: DefinitionId,
        position: Vec3F,
        orientationForward: Vec3F,
        orientationUp: Vec3F
    ): String

    fun placeInGrid(
        blockDefinitionId: DefinitionId,
        gridId: String,
        minPosition: Vec3I,
        orientationForward: Vec3I,
        orientationUp: Vec3I
    ): String

    fun remove(blockId: BlockId)
    fun setIntegrity(blockId: BlockId, integrity: Float)
}

interface CharacterAdmin {
    /**
     * @param position A position within the game scenario itself in system coordinates, uses game units.
     * @param orientationForward Forward vector together with up vector define the character orientation.
     *      Either both or none of them have to be set. The orientation is relative to the system.
     *      They should be normalised vectors and perpendicular.
     * @param orientationUp Complementary vector to the forward vector.
     */
    fun teleport(position: Vec3F, orientationForward: Vec3F? = null, orientationUp: Vec3F? = null): CharacterObservation

    /**
     * @param blockId Id of the block.
     * @param functionIndex Index of useObject. First one is 0 and so on.
     * @param action Action flag, to get possible actions of the block, check [spaceEngineers.model.UseObject.primaryAction] and [spaceEngineers.model.UseObject.secondaryAction] values.
     */
    fun use(blockId: BlockId, functionIndex: Int, action: Int)

    fun create(id: String, position: Vec3F, orientationForward: Vec3F, orientationUp: Vec3F): CharacterObservation

    fun switch(id: String)
}
