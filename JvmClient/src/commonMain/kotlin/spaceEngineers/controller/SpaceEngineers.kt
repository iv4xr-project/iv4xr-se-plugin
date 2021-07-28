package spaceEngineers.controller

import spaceEngineers.model.*

interface SpaceEngineers {
    val session: Session
    val character: Character
    val items: Items
    val observer: Observer
    val definitions: Definitions
    val blocks: Blocks
    val admin: SpaceEngineersAdmin
}

interface Session {
    fun loadScenario(scenarioPath: String)
}

interface Character {
    fun use()
    /**
     * @param movement Unit vector representing direction of a movement.
     *      The direction is relative to the character itself, not coordinates of the system.
     * @see Vec3.FORWARD and other constants for examples.
     * @param rotation3 Unit vector representing rotation of the camera. It's relative to the character itself.
     *      If the character stands on a platform, this moves the camera only.
     *      If the character uses jetpack, this moves the whole character orientation.
     * @see Vec2.ROTATE_UP and other constants for examples.
     */
    fun moveAndRotate(movement: Vec3 = Vec3.ZERO, rotation3: Vec2 = Vec2.ZERO, roll: Float = 0f): CharacterObservation
    fun turnOnJetpack(): CharacterObservation
    fun turnOffJetpack(): CharacterObservation

    fun beginUsingTool()
    fun endUsingTool()

    companion object {
        /**
         * The distance between position of the camera (head) and the position of the character (center) in game units.
         */
        const val DISTANCE_CENTER_CAMERA = 1.6369286f
    }
}

interface Observer {
    fun observe(): CharacterObservation
    fun observeBlocks(): Observation
    fun observeNewBlocks(): Observation
    fun takeScreenshot(absolutePath: String)
}

interface Items {
    fun equip(toolbarLocation: ToolbarLocation)
    fun setToolbarItem(name: String, toolbarLocation: ToolbarLocation)
    fun getToolbar(): Toolbar
}

interface Blocks {
    fun place()
}

interface Definitions {
    fun blockDefinitions(): List<BlockDefinition>
    fun allDefinitions(): List<DefinitionBase>
}
