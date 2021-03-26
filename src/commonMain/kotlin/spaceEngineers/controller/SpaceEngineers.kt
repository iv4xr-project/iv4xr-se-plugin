package spaceEngineers.controller

import spaceEngineers.model.SeObservation
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3

interface SpaceEngineers {
    val session: Session
    val character: Character
    val items: Items
    val observer: Observer
}

interface Session : WorldController

interface Character {
    fun moveAndRotate(movement: Vec3 = Vec3.ZERO, rotation3: Vec3 = Vec3.ZERO, roll: Float = 0f): SeObservation
}

interface Observer {
    fun observe(): SeObservation
    fun observeBlocks(): SeObservation
    fun observeNewBlocks(): SeObservation
    fun observeEntities(): SeObservation
}

interface Items {
    fun place()
    fun equip(toolbarLocation: ToolbarLocation)
    fun startUsingTool()
    fun endUsingTool()
}