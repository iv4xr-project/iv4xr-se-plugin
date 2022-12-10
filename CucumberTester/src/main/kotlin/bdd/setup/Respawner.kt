package bdd.setup

import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import bdd.repetitiveassert.repeatUntilSuccess
import kotlinx.coroutines.delay
import spaceEngineers.controller.SocketReaderWriterException
import spaceEngineers.controller.SpaceEngineers
import bdd.connection.ConnectionManager
import bdd.connection.ProcessWithConnection
import spaceEngineers.controller.extensions.toNullIfMinusOne
import spaceEngineers.controller.extensions.typedFocusedScreen
import spaceEngineers.model.ScreenName
import spaceEngineers.model.ScreenName.Companion.Medicals
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcError
import spaceEngineers.util.whileWithTimeout
import kotlin.time.Duration.Companion.milliseconds

class Respawner(
    override val connectionManager: ConnectionManager,
    val realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(connectionManager),
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config),
    val killClient: Boolean = false,
) : RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase, ConnectionManagerUser by realConnectionManagerUser {

    suspend fun ProcessWithConnection.killIfNeeded() {
        if (gameProcess.isMainClient() || killClient) {
            delay(50)
            kotlin.runCatching {
                dieAndConfirm()
            }
            waitForMedicalScreen()
        }
    }

    private fun ConnectionManagerUser.prepareCharacter() {
        //TODO: ensure we are in the correct scenario
        games {
            when (val focusedScreen = screens.typedFocusedScreen()) {
                ScreenName.GamePlay -> {
                    killIfNeeded()
                }

                Medicals -> {
                }

                ScreenName.MainMenu -> {
                    //connectClientsDirectly()
                }

                ScreenName.Terminal -> {
                    screens.terminal.close()
                    killIfNeeded()
                }
                ScreenName.Loading -> {
                    waitForMedicalScreen()
                }

                ScreenName.CubeBuilder -> {
                    screens.toolbarConfig.close()
                    killIfNeeded()
                }

                ScreenName.MessageBox -> {
                    val message = screens.messageBox.data()
                    when (message.caption) {
                        else -> error("Don't know what to do with MessageBox with caption ${message.caption}, text ${message.text} and type ${message.buttonType}")
                    }
                }

                else -> {
                    error("Don't know what to do with screen $focusedScreen")
                }
            }
        }
    }

    suspend fun respawn(mainMedbay: String, observerMedbay: String, faction: String) {
        prepareCharacter()
        nonMainClientGameObservers {
            if (screens.typedFocusedScreen() == Medicals) {
                checkFaction(faction)
                delay(2500)
                respawn(observerMedbay)
            }
        }
        mainClient {
            checkFaction(faction)
            delay(2500)
            respawn(mainMedbay)
            waitForGameplay()
        }
        delay(100)
    }

    private suspend fun SpaceEngineers.respawn(
        medbay: String,
        waitTimeout: Long = 10_000L,
    ) =
        repeatUntilSuccess(
            initialDelayMs = 0,
            repeats = 10,
            delayMs = 2_500,
            swallowedExceptionTypes = setOf(SocketReaderWriterException::class, KotlinJsonRpcError::class)
        ) {
            with(screens.medicals) {
                whileWithTimeout(waitTimeout.milliseconds) { data().medicalRooms.isEmpty() }
                val index = data().medicalRooms.indexOfFirst { it.name == medbay }.toNullIfMinusOne()
                check(index != null) {
                    "Spawn point '$medbay' not found, found: ${
                        data().medicalRooms.map { it.name }.sorted()
                    }"
                }
                delay(200L * (repeatIndex + 1))
                selectRespawn(index)
                delay(500L * (repeatIndex + 1))
                respawn()
                delay(200L * (repeatIndex + 1))
            }
        }

    private suspend fun SpaceEngineers.checkFaction(faction: String) {
        if (screens.typedFocusedScreen() != Medicals) {
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
                //TODO: throw for -1 on server
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
