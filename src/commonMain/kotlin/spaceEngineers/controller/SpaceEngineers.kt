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
    fun moveAndRotate(movement: Vec3 = Vec3.ZERO, rotation3: Vec3 = Vec3.ZERO, roll: Float = 0f): SeObservation
}

interface Observer {
    fun observe(): SeObservation
    fun observeBlocks(): SeObservation
    fun observeNewBlocks(): SeObservation
    fun takeScreenshot(absolutePath: String)
}

interface Items {
    fun place()
    fun equip(toolbarLocation: ToolbarLocation)
    fun beginUsingTool()
    fun endUsingTool()
    fun setToolbarItem(name: String, location: ToolbarLocation)
    fun getToolbar(): Toolbar
}

interface Definitions {
    fun blockDefinitions(): List<SeBlockDefinition>
}
