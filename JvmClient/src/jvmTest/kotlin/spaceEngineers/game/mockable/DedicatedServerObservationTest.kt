package spaceEngineers.game.mockable

import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import testhelp.MockOrRealGameTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class DedicatedServerObservationTest : MockOrRealGameTest(loadScenario = true, forceRealGame = true, port = 3333u) {

    @Test
    fun testLoad() = testContext {
    }

    @Test
    fun testAdminObserveCharacters() = testContext {
        admin.observer.observeCharacters().apply(::println)
    }

    @Test
    fun testObserveCharacters() = testContext {
        observer.observeCharacters().apply(::println)
    }

    @Test
    fun testAdminPlaceAt() = testContext {
        val characterObservation = admin.observer.observeCharacters().first()
        admin.character.switch(characterObservation.id)
        admin.blocks.placeAt(DefinitionId.cubeBlock("LargeBlockArmorBlock"), characterObservation.position, Vec3F.FORWARD, Vec3F.UP)
    }

    @Test
    fun testTeleport() = testContext {
        val characterObservation = admin.observer.observeCharacters().first()
        admin.character.switch(characterObservation.id)
        admin.character.teleport(characterObservation.position + characterObservation.orientationForward * 15f)
    }

    @Test
    fun definitions() = testContext {
        val definitions = definitions.blockDefinitions()
        println(definitions.first())
        println(definitions.size)
    }

    @Test
    fun connect() = testContext {
        session.connect("127.0.0.1:27016")
    }
}
