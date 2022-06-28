package bdd.setup

import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import bdd.repetitiveassert.repeatUntilSuccess
import bdd.waitForGameplay
import kotlinx.coroutines.delay
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SocketReaderWriterException
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.extensions.toNullIfNegative1
import spaceEngineers.model.canUse
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcError
import spaceEngineers.util.whileWithTimeout

class Respawner(
    override val connectionManager: ConnectionManager,
    val realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(connectionManager),
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config),
) : RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase, ConnectionManagerUser by realConnectionManagerUser {


    suspend fun respawn(mainMedbay: String, observerMedbay: String, faction: String) {
        nonMainClientGameObservers {
            checkFactionAndRespawn(faction, observerMedbay)
        }
        mainClient {
            checkFactionAndRespawn(faction, mainMedbay)
            waitForGameplay()
        }
        delay(100)
    }

    private suspend fun ContextControllerWrapper.checkFactionAndRespawn(
        faction: String,
        observerMedbay: String
    ) {
        checkFaction(faction)
        respawn(observerMedbay)
    }


    private suspend fun SpaceEngineers.respawn(
        medbay: String,
        waitTimeout: Long = 10_000L,
    ) =
        with(screens.medicals) {
            whileWithTimeout(waitTimeout) { data().medicalRooms.isEmpty() }
            val index = data().medicalRooms.indexOfFirst { it.name == medbay }.toNullIfNegative1()
            check(index != null) {
                "Spawn point '$medbay' not found, found: ${
                    data().medicalRooms.map { it.name }.sorted()
                }"
            }
            selectRespawn(index)
            whileWithTimeout(waitTimeout) { data().run { showFactions || !respawnButton.canUse } }
            respawn()
        }

    private suspend fun SpaceEngineers.checkFaction(faction: String) {
        if (screens.focusedScreen() != "Medicals") {
            return
        }
        with(screens.medicals) {

            val data = data()
            if (!data.showFactions) {
                return@with
            }
            repeatUntilSuccess(
                initialDelayMs = 0,
                repeats = 10,
                delayMs = 100,
                swallowedExceptionTypes = setOf(SocketReaderWriterException::class, KotlinJsonRpcError::class)
            ) {
                val index = data.factions.indexOfFirst { it.name == faction }
                check(index != -1) { "Faction '$faction' not found, found ${data.factions.map { it.name }}" }
                selectFaction(index)
                delay(500L * repeatIndex)
                if (!data().showFactions) {
                    return@repeatUntilSuccess
                }
                //join()
            }
        }
    }
}
