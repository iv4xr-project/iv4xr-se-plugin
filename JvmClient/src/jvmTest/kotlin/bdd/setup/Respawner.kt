package bdd.setup

import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import bdd.repetitiveassert.repeatUntilSuccess
import bdd.waitForGameplay
import kotlinx.coroutines.delay
import spaceEngineers.controller.SocketReaderWriterException
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcError

class Respawner(
    override val connectionManager: ConnectionManager,
    val realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(connectionManager),
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config),
) : RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase, ConnectionManagerUser by realConnectionManagerUser {


    suspend fun respawn(mainMedbay: String, observerMedbay: String, faction: String) {
        nonMainClientGameObservers {
            checkFaction(faction)
            delay(500)
            respawn(observerMedbay)
        }
        mainClient {
            checkFaction(faction)
            delay(500)
            respawn(mainMedbay)
            waitForGameplay()
        }
        delay(100)
    }

    private suspend fun SpaceEngineers.respawn(medbay: String) = with(screens.medicals) {
        repeatUntilSuccess(
            initialDelayMs = 0,
            repeats = 10,
            delayMs = 1_500,
            swallowedExceptionTypes = setOf(SocketReaderWriterException::class, KotlinJsonRpcError::class)
        ) {
            val index = data().medicalRooms.indexOfFirst { it.name == medbay }
            //TODO: throw for -1 on server
            check(index != -1) { "Spawn point '$medbay' not found, found: ${data().medicalRooms.map { it.name }.sorted()}" }
            delay(200L * repeatIndex)
            selectRespawn(index)
            delay(200L * repeatIndex)
            respawn()
            delay(200L * repeatIndex)
        }
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
                //TODO: throw for -1 on server
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
