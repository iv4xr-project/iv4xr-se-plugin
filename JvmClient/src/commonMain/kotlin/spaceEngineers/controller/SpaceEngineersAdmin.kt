package spaceEngineers.controller

import spaceEngineers.model.BlockId
import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3

interface SpaceEngineersAdmin {
    val blocks: BlocksAdmin
    val character: CharacterAdmin
    fun setFrameLimitEnabled(enabled: Boolean)
}

interface BlocksAdmin {
    fun placeAt(blockDefinitionId: DefinitionId, position: Vec3, orientationForward: Vec3, orientationUp: Vec3): String
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
    fun teleport(position: Vec3, orientationForward: Vec3? = null, orientationUp: Vec3? = null): CharacterObservation
    fun use(blockId: BlockId, functionIndex: Int, action: Int)
}
