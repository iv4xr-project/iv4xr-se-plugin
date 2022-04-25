package spaceEngineers.controller

import spaceEngineers.model.*
import spaceEngineers.navigation.NavGraph

interface SpaceEngineers {
    val session: Session
    val character: Character
    val items: Items
    val observer: Observer
    val definitions: Definitions
    val blocks: Blocks
    val admin: SpaceEngineersAdmin
    val screens: Screens
    val pause: Pause

    companion object {
        const val DEFAULT_AGENT_ID = "se0"
    }
}

interface Pause {
    fun setPaused(paused: Boolean)
    fun isPaused(): Boolean
}

interface Session {
    fun loadScenario(scenarioPath: String)
    fun connect(address: String)
    fun disconnect()
    fun exitGame()
    fun exitToMainMenu()
}

interface Character {
    fun use()

    /**
     * @param movement Unit vector representing direction of a movement.
     *      The direction is relative to the character itself, not coordinates of the system.
     * @see Vec3F.FORWARD and other constants for examples.
     * @param rotation3 Unit vector representing rotation of the camera. It's relative to the character itself.
     *      If the character stands on a platform, this moves the camera only.
     *      If the character uses jetpack, this moves the whole character orientation.
     * @see Vec2F.ROTATE_UP and other constants for examples.
     */
    fun moveAndRotate(
        movement: Vec3F = Vec3F.ZERO,
        rotation3: Vec2F = Vec2F.ZERO,
        roll: Float = 0f,
        ticks: Int = 1,
    ): CharacterObservation
    fun jump(movement: Vec3F = Vec3F.ZERO)

    fun turnOnJetpack(): CharacterObservation
    fun turnOffJetpack(): CharacterObservation
    fun turnOnDampeners(): CharacterObservation
    fun turnOffDampeners(): CharacterObservation
    fun switchHelmet(): CharacterObservation

    fun beginUsingTool()
    fun endUsingTool()
    fun showTerminal()
    fun showInventory()


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
    fun observeCharacters(): List<CharacterObservation>
    fun observeFloatingObjects(): List<FloatingObject>
    fun navigationGraph(): NavGraph

    /**
     * Creates screenshot in the game. If there's an error, no exception is thrown (swallowed by the game itself).
     * Creates directories that do not exist if necessary.
     * If this method doesn't do anything it's usually a problem with the path or permissions on the filesystem.
     * @param absolutePath Absolute path to final image. For example `C:/Screenshots/image.png`
     */
    fun takeScreenshot(absolutePath: String)
    fun switchCamera()
}

interface Items {
    fun equip(toolbarLocation: ToolbarLocation)
    fun setToolbarItem(definitionId: DefinitionId, toolbarLocation: ToolbarLocation)
    fun toolbar(): Toolbar
}

interface Blocks {
    fun place()
}

interface Definitions {
    fun blockDefinitions(): List<BlockDefinition>
    fun allDefinitions(): List<DefinitionBase>
    fun blockHierarchy(): Map<String, String>
    fun blockDefinitionHierarchy(): Map<String, String>
}
