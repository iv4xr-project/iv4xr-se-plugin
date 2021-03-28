package environments

import eu.iv4xr.framework.mainConcepts.W3DAgentState
import eu.iv4xr.framework.mainConcepts.W3DEnvironment
import eu.iv4xr.framework.mainConcepts.WorldEntity
import eu.iv4xr.framework.mainConcepts.WorldModel
import spaceEngineers.controller.*
import spaceEngineers.model.*
import java.io.File

fun Vec3.toIv4xrVec3(): eu.iv4xr.framework.spatial.Vec3 {
    return eu.iv4xr.framework.spatial.Vec3(x, y, z)
}

val WorldEntity.integrity: Int
    get() {
        return properties["integrity"] as Int
    }

val WorldEntity.maxIntegrity: Int
    get() {
        return properties["maxIntegrity"] as Int
    }

val WorldEntity.buildIntegrity: Int
    get() {
        return properties["buildIntegrity"] as Int
    }

val WorldEntity.blockType: String
    get() {
        return properties["blockType"] as String
    }

fun SeGrid.toWorldEntity(): WorldEntity {
    return WorldEntity(this.id, "grid", false).also { we ->
        we.position = position.toIv4xrVec3()
        we.elements = blocks.map { it.toWorldEntity() }.associateBy { it.id }
    }
}

fun SeBlock.toWorldEntity(): WorldEntity {
    return WorldEntity(id, "block", false).also { we ->
        we.position = position.toIv4xrVec3()
        we.properties["blockType"] = blockType
        we.properties["integrity"] = integrity
        we.properties["maxIntegrity"] = maxIntegrity
        we.properties["buildIntegrity"] = buildIntegrity
        size?.let {
            we.extent = it.toIv4xrVec3()
        }
    }
}

fun SeObservation.toWorldModel(): WorldModel {
    return WorldModel().also { worldModel ->
        worldModel.agentId = agentID
        worldModel.position = position.toIv4xrVec3()
        worldModel.velocity = velocity.toIv4xrVec3()
        worldModel.elements = grids?.map { it.toWorldEntity() }?.associateBy { it.id } ?: emptyMap()
    }
}

val W3DAgentState.seEnv: SeEnvironment
    get() = env() as SeEnvironment

fun W3DAgentState.setOrUpdate(worldModel: WorldModel) {
    if (wom == null) {
        wom = worldModel
    } else {
        wom.mergeNewObservation(worldModel)
    }
}

fun W3DAgentState.observe() {
    setOrUpdate(seEnv.observe())
}

class SeEnvironment(
    val worldId: String,
    val controller: ContextControllerWrapper,
    val worldController: WorldController,
    val context: SpaceEngineersTestContext
) : W3DEnvironment() {
    val SCENARIO_DIR = "src/jvmTest/resources/game-saves/"


    override fun loadWorld() {
        val scenario = File("$SCENARIO_DIR$worldId").absolutePath
        worldController.loadScenario(scenario)
    }

    override fun observe(agentId: String): WorldModel {
        return observe()
    }

    fun observe(): WorldModel {
        return controller.observer.observe().toWorldModel()
    }

    fun observeForNewBlocks(): WorldModel {
        return controller.observer.observeNewBlocks().toWorldModel()
    }

    fun equipAndPlace(toolbarLocation: ToolbarLocation) {
        controller.items.equip(toolbarLocation)
        return controller.items.place()
    }

    fun equip(toolbarLocation: ToolbarLocation) {
        return controller.items.equip(toolbarLocation)
    }

    fun startUsingTool() {
        return controller.items.startUsingTool()
    }

    fun endUsingTool() {
        return controller.items.endUsingTool()
    }


    fun moveForward(velocity: Float = 1f): WorldModel {
        return controller.character.moveForward(velocity).toWorldModel()
    }


    fun equipAndPlace(blockType: String) {
        return equipAndPlace(context.blockToolbarLocation(blockType))
    }
}