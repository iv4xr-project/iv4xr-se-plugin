package bdd.setup

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.controller.SpaceEngineers
import bdd.connection.AppType
import spaceEngineers.controller.extensions.typedFocusedScreen
import spaceEngineers.controller.extensions.waitForScreen
import spaceEngineers.controller.extensions.waitForScreenFinish
import spaceEngineers.model.*
import spaceEngineers.model.ScreenName.Companion.CubeBuilder
import spaceEngineers.model.ScreenName.Companion.GamePlay
import spaceEngineers.model.ScreenName.Companion.JoinGame
import spaceEngineers.model.ScreenName.Companion.Loading
import spaceEngineers.model.ScreenName.Companion.MainMenu
import spaceEngineers.model.ScreenName.Companion.Medicals
import spaceEngineers.model.ScreenName.Companion.MessageBox
import spaceEngineers.model.ScreenName.Companion.Progress
import spaceEngineers.model.ScreenName.Companion.SaveAs
import spaceEngineers.model.ScreenName.Companion.ServerConnect
import spaceEngineers.model.ScreenName.Companion.Terminal
import spaceEngineers.model.ScreenName.Companion.ToolbarConfig
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

suspend fun SpaceEngineers.dieAndConfirm(delayMs: Long = 100L) {
    //TODO: die only if not dead
    admin.character.die()
    delay(delayMs)
    val fs = screens.typedFocusedScreen()
    check(fs == MessageBox) { fs }
    screens.messageBox.pressYes()
}

fun SpaceEngineers.ensureCamera(cameraConfig: CameraConfig) {
    val info = session.info()
    val cameraInfo = info?.camera ?: error("No camera info")
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

suspend fun ConnectionManagerUser.handleScenarioParameter(key: String, value: String) {
    when (key) {
        "delay_after_spawn" -> delay((value.toFloat() * 1000f).toLong())
        "energy" -> {
            val id = mainClient {
                admin.character.updateEnergy(energy = value.toFloat() / 100f)
                admin.character.mainCharacterId()
            }
            admin {
                admin.character.switch(id)
                admin.character.updateEnergy(energy = value.toFloat() / 100f)
            }
        }

        "hydrogen" -> {
            val id = mainClient {
                admin.character.updateHydrogen(hydrogen = value.toFloat() / 100f)
                admin.character.mainCharacterId()
            }
            admin {
                admin.character.switch(id)
                admin.character.updateHydrogen(hydrogen = value.toFloat() / 100f)
            }
        }

        "health" -> {
            val id = mainClient {
                admin.character.updateHealth(health = value.toFloat() / 100f)
                admin.character.mainCharacterId()
            }
            admin {
                admin.character.switch(id)
                admin.character.updateHealth(health = value.toFloat() / 100f)
            }
        }

        "oxygen" -> {
            val id = mainClient {
                admin.character.updateOxygen(oxygen = value.toFloat() / 100f)
                admin.character.mainCharacterId()
            }
            admin {
                admin.character.switch(id)
                admin.character.updateOxygen(oxygen = value.toFloat() / 100f)
            }
        }

        "camera" -> {
            mainClient { ensureCamera(CameraConfig.fromText(value)) }
        }

        else -> error("Warning, unknown settings: $key - $value")
    }
}

suspend fun ConnectionManagerUser.handleScenarioParameters(data: Map<String, String>) {
    val alreadyHandledParameters = setOf("faction", "scenario", "main_client_medbay", "observer_medbay")
    data.filter { it.key !in alreadyHandledParameters }
        .forEach { (key, value) ->
            handleScenarioParameter(key, value)
        }
}

suspend fun ExtendedSpaceEngineers.getToMedicalScreen() {
    when (val focusedScreen = screens.typedFocusedScreen()) {
        Medicals -> {}
        Terminal, ToolbarConfig, SaveAs, CubeBuilder -> {
            extensions.screen.navigation.navigateTo(GamePlay)
            dieAndConfirm()
        }

        GamePlay -> {
            dieAndConfirm()
        }

        MainMenu -> {
            val data = screens.mainMenu.data()
            if (data.type == MainMenuData.MAIN) {
                //TODO: connect to the game
            } else {
                extensions.screen.navigation.navigateTo(GamePlay)
            }
        }

        MessageBox -> {
            val message = screens.messageBox.data()
            when (message.caption) {
                else -> error("Don't know what to do with MessageBox with caption ${message.caption}, text ${message.text} and type ${message.buttonType}")
            }
        }

        Loading -> {
            screens.waitForScreenFinish(20.seconds, screenName = Loading)
        }

        Progress -> {
            screens.waitForScreenFinish(20.seconds, screenName = Progress)
        }

        else -> {
            error("Don't know what to do with screen $focusedScreen")
        }
    }
}


fun ConnectionManagerUser.connectClientsDirectly(waitForMedical: Boolean = true) = runBlocking {
    clients {
        connectDirectly(connectionManager.admin.gameProcess.address)
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


suspend fun SpaceEngineers.waitForMedicalScreen() {
    screens.waitForScreen(timeout = 40_321.milliseconds, screenName = Medicals)
}

suspend fun SpaceEngineers.waitForJoinGameScreen() {
    screens.waitForScreen(timeout = 40_322.milliseconds, screenName = JoinGame)
}

suspend fun SpaceEngineers.waitForServerConnectScreen() {
    screens.waitForScreen(timeout = 40_323.milliseconds, screenName = ServerConnect)
}

suspend fun SpaceEngineers.waitForGameplay() {
    screens.waitForScreen(timeout = 60_001.milliseconds, screenName = GamePlay)
}


