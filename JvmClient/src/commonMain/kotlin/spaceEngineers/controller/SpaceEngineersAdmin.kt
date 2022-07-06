package spaceEngineers.controller

import spaceEngineers.model.*

interface SpaceEngineersAdmin {
    val blocks: BlocksAdmin
    val character: CharacterAdmin
    val observer: ObserverAdmin
    fun setFrameLimitEnabled(enabled: Boolean)
    fun updateDefaultInteractDistance(distance: Float)
    fun debugInfo(): DebugInfo
    val tests: TestAdmin
    fun showNotification(text: String)
}

interface TestAdmin {
    fun adminOnly()
    fun gameOnly()
    fun observerOnly()
}

interface BlocksAdmin {
    fun placeAt(
        blockDefinitionId: DefinitionId,
        position: Vec3F,
        orientationForward: Vec3F,
        orientationUp: Vec3F,
        color: Vec3F? = null,
    ): String

    fun placeInGrid(
        blockDefinitionId: DefinitionId,
        gridId: String,
        minPosition: Vec3I,
        orientationForward: Vec3I,
        orientationUp: Vec3I,
        color: Vec3F? = null,
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

    fun create(name: String, position: Vec3F, orientationForward: Vec3F, orientationUp: Vec3F): CharacterObservation

    fun switch(id: String)

    fun remove(id: String)
    fun showTerminal(blockId: String)
    fun die()
    fun mainCharacterId(): String
    fun localCharacterId(): String?
}

interface ObserverAdmin {
    fun observeCharacters(): List<CharacterObservation>
}
