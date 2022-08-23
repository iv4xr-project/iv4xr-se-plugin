package bdd.setup

import io.cucumber.java.Scenario
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.AppType
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ProcessWithConnection
import spaceEngineers.controller.connection.ScreenshotMode
import spaceEngineers.controller.extensions.waitForScreen
import spaceEngineers.model.canUse
import spaceEngineers.util.whileWithTimeout
import kotlin.test.assertEquals

suspend fun SpaceEngineers.dieAndConfirm(delayMs: Long = 100L) {
    admin.character.die()
    delay(delayMs)
    val fs = screens.focusedScreen()
    check(fs == "MessageBox") { fs }
    screens.messageBox.pressYes()
}

suspend fun SpaceEngineers.ensureCamera(cameraConfig: CameraConfig) {
    val info = session.info()
    val cameraInfo = info.camera ?: error("No camera info")
    when (cameraConfig) {
        CameraConfig.FIRST_PERSON -> {
            if (!cameraInfo.isInFirstPersonView) {
                observer.switchCamera()
            }
        }
        CameraConfig.THIRD_PERSON -> {
            if (cameraInfo.isInFirstPersonView) {
                observer.switchCamera()
            }
        }
    }
}

suspend fun ConnectionManagerUser.handleScenarioParameters(data: Map<String, String>) {
    data["delay_after_spawn"]?.toFloatOrNull()?.let { delaySeconds ->
        delay((delaySeconds * 1000f).toLong())
    }
    data["energy"]?.let {
        admin { admin.character.updateEnergy(energy = it.toFloat()) }
    }
    data["hydrogen"]?.let {
        admin { admin.character.updateHydrogen(hydrogen = it.toFloat()) }
    }
    data["oxygen"]?.let {
        admin { admin.character.updateOxygen(oxygen = it.toFloat()) }
    }
    data["camera"]?.let { it ->
        mainClient {
            ensureCamera(CameraConfig.fromText(it))
        }
    }
}

fun ConnectionManagerUser.ensureEveryoneIsSameSession() {
    val sessionInfos = all {
        session.info()
    }
    assertEquals(sessionInfos.distinctBy { it.name }.size, 1)
    assertEquals(sessionInfos.distinctBy { it.settings }.size, 1)
}

fun ConnectionManagerUser.prepareCharacter() {
    //TODO: ensure we are in the correct scenario
    games {
        when (val focusedScreen = screens.focusedScreen()) {
            "GamePlay" -> {
                dieAndConfirm()
                waitForMedicalScreen()
            }
            "Medicals" -> {
            }
            "MainMenu" -> {
                //connectClientsDirectly()
            }
            "Terminal" -> {
                screens.terminal.close()
                delay(50)
                dieAndConfirm()
                waitForMedicalScreen()
            }
            "CubeBuilder" -> {
                screens.toolbarConfig.close()
                delay(50)
                dieAndConfirm()
                waitForMedicalScreen()
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
        screens.mainMenu.joinGame()
        waitForJoinGameScreen()
        screens.joinGame.directConnect()
        waitForServerConnectScreen()
        screens.serverConnect.enterAddress("${process.address}:27016")
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
        waitForJoinGameScreen()
        with(screens.joinGame) {
            selectTab(5)
            whileWithTimeout {
                data().selectedTab != 5 || data().games.isEmpty()
            }
            selectGame(0)
            whileWithTimeout { !data().joinWorldButton.canUse }
            joinWorld()
        }
        smallPause()
        screens.waitUntilTheGameLoaded()
    }
    runBlocking {
        pause()
    }
}

suspend fun SpaceEngineers.waitForMedicalScreen() {
    screens.waitForScreen(timeoutMs = 40_321, screenName = "Medicals")
}

suspend fun SpaceEngineers.waitForJoinGameScreen() {
    screens.waitForScreen(timeoutMs = 40_322, screenName = "JoinGame")
}

suspend fun SpaceEngineers.waitForServerConnectScreen() {
    screens.waitForScreen(timeoutMs = 40_323, screenName = "ServerConnect")
}


suspend fun SpaceEngineers.waitForGameplay() {
    screens.waitForScreen(timeoutMs = 60_001, screenName = "GamePlay")
}

fun shouldTakeScreenshot(scenario: Scenario, screenshotMode: ScreenshotMode): Boolean {
    return when (screenshotMode) {
        ScreenshotMode.NEVER -> false
        ScreenshotMode.ON_FAILURE -> scenario.isFailed
        ScreenshotMode.ALWAYS -> true
    }
}

suspend fun ConnectionManager.processScreenshots(scenario: Scenario) {
    if (!shouldTakeScreenshot(scenario, config.screenshotMode)) {
        return
    }
    games {
        processScreenshot(scenario)
    }
}

suspend fun ProcessWithConnection.processScreenshot(scenario: Scenario) {

}
