package spaceEngineers.game.mockable

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3F
import testhelp.MockOrRealGameTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class MultiAgentTest : MockOrRealGameTest() {


    val DEFAULT_ID = "main"

    @Test
    fun createCharacter() = testContext {
        admin.character.switch(DEFAULT_ID)
        val observation = observer.observe()

        val observation2 = admin.character.create(
            "mybot",
            observation.position,
            observation.orientationForward,
            observation.orientationUp
        )
        println(observation)
        println(observation2)
    }

    @Test
    fun moveCharacter() = testContext {
        admin.character.switch("mybot")
        val position = observer.observe().position
        //admin.character.switch(DEFAULT_ID)
        //character.turnOnJetpack()
        //character.turnOffJetpack()
        //character.moveAndRotate(rotation3 = Vec2F.ROTATE_DOWN, ticks = 20)
        //character.moveAndRotate(movement = Vec3F.RIGHT, ticks = 2)
        character.use()
        //admin.character.teleport(position + Vec3F.FORWARD * 5f)
    }

    @Test
    fun equip() = testContext {
        admin.character.switch("mybot")
        val location = ToolbarLocation(2, 0)
        items.setToolbarItem("Welder2Item", location)
        items.equip(location)
    }


    @Test
    fun grindDown() = testContext {
        admin.character.switch("mybot")
        val location = ToolbarLocation(1, 0)
        items.setToolbarItem("AngleGrinder2Item", location)
        items.equip(location)
        //character.beginUsingTool()
        //admin.character.teleport(position + Vec3F.FORWARD * 5f)
    }

    @Test
    fun beginUsingTool() = testContext {
        admin.character.switch("mybot")
        character.beginUsingTool()
    }

    @Test
    fun build() = testContext {
        //admin.character.switch("mybot")
        admin.character.switch(DEFAULT_ID)
        val location = ToolbarLocation(0, 0)
        items.setToolbarItem("LargeHeavyBlockArmorBlock", location)
        items.equip(location)
        blocks.place()
    }

    @Test
    fun adminBuild() = testContext {
        admin.character.switch("mybot")
        admin.blocks.placeAt(DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock"), observer.observe().position, Vec3F.FORWARD, Vec3F.UP)
    }
}
