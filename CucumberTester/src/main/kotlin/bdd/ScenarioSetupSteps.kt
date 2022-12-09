package bdd


import bdd.setup.*
import io.cucumber.datatable.DataTable
import io.cucumber.java.*
import io.cucumber.java.en.Given
import kotlinx.coroutines.delay
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.loadFromTestResources
import testhelp.hideUndeclaredThrowableException
import kotlin.time.Duration.Companion.seconds


lateinit var testSetup: TestSetup

/*
SINGLE_COMPUTER_DEDICATED_DEV_KAREL    OFFLINE_STEAM
LOBBY_STEAM DS_STEAM
*/



fun testSetupFactory(config: ConnectionSetup, connectionManager: ConnectionManager): TestSetup {
    return if (config.ds) {
        MedbayDSSetup(config, connectionManager)
    } else if (config.lobby) {
        MedbayLobbySetup(config, connectionManager)
    } else {
        error("Mad factory!")
    }
}

@BeforeAll
fun beforeAll() {
    if (!::testSetup.isInitialized) {
        testSetup = testSetupFactory(globalCm.connectionSetup, globalCm)
    }
    testSetup.beforeAll()
}

@AfterAll
fun afterAll() {
    testSetup.afterAll()
    //exitToMenuAndCloseEverything()
}

@Before
fun before(scenario: Scenario) {
    testSetup.beforeScenario(scenario)
}

@After
fun after(scenario: Scenario) {
    testSetup.afterScenario(scenario)
}


class ScenarioSetupSteps(connectionManager: ConnectionManager) : AbstractMultiplayerSteps(connectionManager) {


    @Given("Scenario used is {string}.")
    fun scenario_used_is(scenarioId: String) = hideUndeclaredThrowableException {
        mainClient {
            session.loadFromTestResources(scenarioId, "src/main/resources/game-saves/")
            screens.waitUntilTheGameLoaded()
            delay(1.seconds)
        }
        observers {
            observer.observeNewBlocks()
        }
    }

    @Given("Scenario config:")
    fun scenario_config(dataTable: DataTable) = hideUndeclaredThrowableException {
        testSetup.scenarioConfig(dataTable.asMaps())
    }


}
