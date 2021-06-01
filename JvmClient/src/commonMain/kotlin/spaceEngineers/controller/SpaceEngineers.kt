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
    fun moveAndRotate(movement: Vec3 = Vec3.ZERO, rotation3: Vec2 = Vec2.ZERO, roll: Float = 0f): Observation
    fun teleport(position: Vec3, orientationForward: Vec3? = null, orientationUp: Vec3? = null): Observation
    fun turnOnJetpack(): Observation
    fun turnOffJetpack(): Observation


    companion object {
        /**
         * Distance between position of camera (head) and position of character (center) in game units.
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
