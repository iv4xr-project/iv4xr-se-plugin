package spaceEngineers.game.mockable

import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.normalizeAsSprint
import testhelp.MockOrRealGameTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class DedicatedServerClientTest : MockOrRealGameTest(loadScenario = false, forceRealGame = true, port = 3334u) {

    @Test
    fun connect() = testContext {
        session.connect("127.0.0.1:27016")
        // admin.observer.observeCharacters().apply(::println)
    }

    @Test
    fun jetpackOff() = testContext {
        character.turnOffJetpack()
    }

    @Test
    fun jetpackOn() = testContext {
        character.turnOnJetpack()
    }

    @Test
    fun use() = testContext {
        character.use()
    }

    @Test
    fun moveForward() = testContext {
        character.moveAndRotate(Vec3F.FORWARD.normalizeAsSprint(), ticks = 100)
    }

    @Test
    fun switchHelmet() = testContext {
        character.setHelmet(true)
    }
}
