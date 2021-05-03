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
}

interface Observer {
    fun observe(): Observation
    fun observeBlocks(): Observation
    fun observeNewBlocks(): Observation
    fun takeScreenshot(absolutePath: String)
}

interface Items {
    fun place()
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
