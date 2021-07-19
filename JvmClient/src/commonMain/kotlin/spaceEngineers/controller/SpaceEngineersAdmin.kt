package spaceEngineers.controller

import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.Vec3

interface SpaceEngineersAdmin {
    val blocks: BlocksAdmin
    val character: CharacterAdmin
    fun setFrameLimitEnabled(enabled: Boolean)
}

interface BlocksAdmin {
    fun placeAt(blockType: String, position: Vec3, orientationForward: Vec3, orientationUp: Vec3)
    fun remove(blockId: String)
    fun setIntegrity(blockId: String, integrity: Float)
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
}
