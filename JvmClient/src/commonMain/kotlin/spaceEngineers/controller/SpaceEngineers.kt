package spaceEngineers.controller

import spaceEngineers.model.*

interface SpaceEngineers {
    val session: Session
    val character: Character
    val items: Items
    val observer: Observer
    val definitions: Definitions
}


interface Session {
    fun loadScenario(scenarioPath: String)
}

interface Character {
    /**
     * @param movement Unit vector representing direction of a movement.
     *      The direction is relative to the character itself, not coordinates of the system.
     * @see Vec3.FORWARD and other constants for examples.
     * @param rotation3 Unit vector representing rotation of the camera. It's relative to the character itself.
     *      If the character stands on a platform, this moves the camera only.
     *      If the character uses jetpack, this moves the whole character orientation.
     * @see Vec2.ROTATE_UP and other constants for examples.
     */
    fun moveAndRotate(movement: Vec3 = Vec3.ZERO, rotation3: Vec2 = Vec2.ZERO, roll: Float = 0f): Observation

    /**
     * @param position A position within the game scenario itself in system coordinates, uses game units.
     * @param orientationForward Forward vector together with up vector define the character orientation.
     *      Either both or none of them have to be set. The orientation is relative to the system.
     *      They should be normalised vectors and perpendicular.
     * @param orientationUp Complementary vector to the forward vector.
     */
    fun teleport(position: Vec3, orientationForward: Vec3? = null, orientationUp: Vec3? = null): Observation
    fun turnOnJetpack(): Observation
    fun turnOffJetpack(): Observation


    companion object {
        /**
         * The distance between position of the camera (head) and the position of the character (center) in game units.
         */
        const val DISTANCE_CENTER_CAMERA = 1.6369286f
    }
}

interface Observer {
    fun observe(): Observation
    fun observeBlocks(): Observation
    fun observeNewBlocks(): Observation
    fun takeScreenshot(absolutePath: String)
}

interface Items {
    fun place()
    fun placeAt(blockType: String, position: Vec3, orientationForward: Vec3, orientationUp: Vec3)
    fun remove(blockId: String)
    fun equip(toolbarLocation: ToolbarLocation)
    fun beginUsingTool()
    fun endUsingTool()
    fun setToolbarItem(name: String, toolbarLocation: ToolbarLocation)
    fun getToolbar(): Toolbar
}

interface Definitions {
    fun blockDefinitions(): List<BlockDefinition>
    fun allDefinitions(): List<DefinitionBase>
}
