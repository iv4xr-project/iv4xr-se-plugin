package bdd

import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import bdd.setup.ConnectionManagerUser
import bdd.setup.GlobalConnectionManagerUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.loadFromTestResources
import testhelp.hideUndeclaredThrowableException

abstract class AbstractMultiplayerSteps(
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config),
    val connectionManagerUser: ConnectionManagerUser = GlobalConnectionManagerUser(),
) : AutoCloseable, RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase, ConnectionManagerUser by connectionManagerUser {

    val cm: ConnectionManager
        get() = testSetup.connectionManager

    override fun close() {
        cm.close()
    }


    fun ensureCharacterExists() = clients {
        if (screens.focusedScreen() == "Medicals") {
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
