package environments

import eu.iv4xr.framework.environments.W3DEnvironment
import eu.iv4xr.framework.mainConcepts.WorldEntity
import eu.iv4xr.framework.mainConcepts.WorldModel
import eu.iv4xr.framework.spatial.meshes.Mesh
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SpaceEngineersTestContext
import spaceEngineers.controller.extensions.moveForward
import spaceEngineers.model.*
import spaceEngineers.model.extensions.blockById
import spaceEngineers.model.extensions.centerPosition
import java.io.File
import java.lang.Thread.sleep

fun Vec3F.toIv4xrVec3(): eu.iv4xr.framework.spatial.Vec3 {
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

fun CubeGrid.toWorldEntity(): WorldEntity {
    return WorldEntity(this.id.toString(), "grid", true).also { we ->
        we.position = position.toIv4xrVec3()
        we.elements = blocks.map { it.toWorldEntity() }.associateBy { it.id }
    }
}

fun Block.toWorldEntity(): WorldEntity {
    return WorldEntity(id.toString(), "block", true).also { we ->
        we.position = position.toIv4xrVec3()
        we.properties["blockType"] = definitionId.type
        we.properties["integrity"] = integrity
        we.properties["maxIntegrity"] = maxIntegrity
        we.properties["buildIntegrity"] = buildIntegrity
        we.extent = size.toIv4xrVec3()
        we.properties["centerPosition"] = centerPosition.toIv4xrVec3()
        we.properties["maxPosition"] = maxPosition.toIv4xrVec3()
        we.properties["minPosition"] = minPosition.toIv4xrVec3()
        we.properties["orientationForward"] = orientationForward.toIv4xrVec3()
        we.properties["orientationUp"] = orientationUp.toIv4xrVec3()
        // Wishpr Hack
        if (this is DoorBase) {
            we.properties["isOpen"] = this.open ;
        }
    }
}

fun Observation.toWorldModel(): WorldModel {
    return character.toWorldModel().also { worldModel ->
        worldModel.elements = grids?.map { it.toWorldEntity() }?.associateBy { it.id } ?: emptyMap()
    }
}

fun CharacterObservation.toWorldModel(): WorldModel {
    return WorldModel().also { worldModel ->
        worldModel.agentId = id.toString()
        worldModel.position = position.toIv4xrVec3()
        worldModel.velocity = velocity.toIv4xrVec3()
        worldModel.elements = emptyMap()
    }
}

class SeEnvironment @JvmOverloads constructor(
    val worldId: String,
    val controller: ContextControllerWrapper,
    val scenarioDir: String = DEFAULT_SCENARIO_DIR,
) : W3DEnvironment(), AutoCloseable {

    val context: SpaceEngineersTestContext = controller.context

    init {
        worldNavigableMesh = Mesh()
    }

    override fun loadWorld() {
        val scenario = File("$scenarioDir$worldId").absolutePath
        controller.session.loadScenario(scenario)
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

    fun getBlock(id: String): Block {
        return controller.observer.blockById(id)
    }

    fun equipAndPlace(toolbarLocation: ToolbarLocation) {
        controller.items.equip(toolbarLocation)
        sleep(500)
        return controller.blocks.place()
    }

    fun equip(toolbarLocation: ToolbarLocation) {
        return controller.items.equip(toolbarLocation)
    }

    fun beginUsingTool() {
        return controller.character.beginUsingTool()
    }

    fun endUsingTool() {
        return controller.character.endUsingTool()
    }

    fun moveForward(): WorldModel {
        return controller.character.moveForward(CharacterMovementType.RUN).toWorldModel()
    }

    fun equipAndPlace(blockType: String) {
        return equipAndPlace(context.blockToolbarLocation(blockType))
    }

    override fun close() {
        controller.close()
    }

    companion object {
        val DEFAULT_SCENARIO_DIR = "src/jvmTest/resources/game-saves/"
    }
}
