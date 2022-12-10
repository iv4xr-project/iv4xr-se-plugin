package bdd

import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import bdd.setup.ConnectionManagerUser
import bdd.setup.RealConnectionManagerUser
import kotlinx.coroutines.delay
import bdd.connection.ConnectionManager
import spaceEngineers.controller.extensions.typedFocusedScreen
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.ScreenName.Companion.Medicals


abstract class AbstractMultiplayerSteps(
    connectionManager: ConnectionManager,
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config),
    val connectionManagerUser: ConnectionManagerUser = RealConnectionManagerUser(connectionManager),
) : AutoCloseable, RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase, ConnectionManagerUser by connectionManagerUser {

    val cm: ConnectionManager
        get() = testSetup.connectionManager

    override fun close() {
        cm.close()
    }


    fun ensureCharacterExists() = clients {
        if (screens.typedFocusedScreen() == Medicals) {
            try {
                screens.medicals.selectFaction(0)
                screens.medicals.join()
            } catch (e: Exception) {

            }
            pause()
            screens.medicals.selectRespawn(0)
            delay(100)
            screens.medicals.respawn()
            pause()
        }
    }

    fun loadScenarioSinglePlayer(scenarioId: String) = mainClient {
        session.loadFromTestResources(scenarioId)
        screens.waitUntilTheGameLoaded()
        smallPause()
    }

    suspend fun pauseAfterAction() {
        delay(1000)
    }

}
