package bdd.setup

import bdd.waitForMedicalScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.AppType
import spaceEngineers.model.Vec3F
import kotlin.random.Random

suspend fun SpaceEngineers.dieAndConfirm(delayMs: Long = 500L) {
    delay(delayMs)
    admin.character.die()
    delay(delayMs)
    val fs = screens.focusedScreen()
    check(fs == "MessageBox") { fs }
    screens.messageBox.pressYes()
    delay(delayMs)

    //check(admin.observer.observeCharacters().isEmpty())
}


fun ConnectionManagerUser.prepareCharacter() {
    //TODO: ensure we are in the correct scenario
    games {
        when (val focusedScreen = screens.focusedScreen()) {
            "GamePlay" -> {
                //val id = mainClient { admin.character.mainCharacterId() }


                dieAndConfirm()

                waitForMedicalScreen()
                pause()
            }
            "Medicals" -> {
            }
            "MainMenu" -> {
                //connectClientsDirectly()
            }
            else -> {
                error("Don't know what to do with screen $focusedScreen")
            }
        }
    }
}


fun ConnectionManagerUser.connectClientsDirectly(waitForMedical: Boolean = true) = runBlocking {
    clients {
        //TODO: if not in main menu, exit to it rather than failing
        val process = connectionManager.admin.gameProcess
        smallPause()
        screens.mainMenu.joinGame()
        smallPause()
        screens.joinGame.directConnect()
        smallPause()
        screens.serverConnect.enterAddress("${process.address}:27016")
        smallPause()
        screens.serverConnect.connect()
        screens.waitUntilTheGameLoaded()
        if (waitForMedical) {
            waitForMedicalScreen()
        }
    }
    pause()
}


fun ConnectionManagerUser.exitToMainMenu(onException: (Throwable) -> Unit = { println(it) }) {
    clients {
        try {
            session.exitToMainMenu()
        } catch (e: Throwable) {
            onException(e)
        }
    }
    if (connectionManager.admin.gameProcess.type == AppType.GAME) {
        admin {
            try {
                session.exitToMainMenu()
            } catch (e: Throwable) {
                onException(e)
            }
        }
    }
}


fun ConnectionManagerUser.createLobbyGame(scenarioId: String, filterSaved: Boolean = true) = admin {
    screens.mainMenu.loadGame()
    pause()
    val data = screens.loadGame.data()
    val filtered = data.files.filter { it.name == scenarioId && (!it.fullName.contains("-saved") || !filterSaved) }
    check(filtered.size < 2) { "Multiple non '-saved' files with same name: $filtered" }
    val index = data.files.indexOfFirst { it.name == scenarioId && (!it.fullName.contains("-saved") || !filterSaved) }
    check(index > -1) {
        "Scenario $scenarioId not found in the list, found: ${data.files.map { it.name }.sorted()}"
    }
    screens.loadGame.doubleClickWorld(index)
    bigPause()
    screens.waitUntilTheGameLoaded()
}

fun ConnectionManagerUser.connectToFirstFriendlyGame() {
    clients {
        screens.mainMenu.joinGame()
        smallPause()
        screens.joinGame.selectTab(5)
        smallPause()

        screens.joinGame.selectGame(0)

        pause()
        screens.joinGame.joinWorld()
        smallPause()
        screens.waitUntilTheGameLoaded()
    }
    runBlocking {
        pause()
    }
}
