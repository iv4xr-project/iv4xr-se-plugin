package bdd

import bdd.setup.MedbayDSSetup
import bdd.setup.MedbayLobbySetup
import bdd.setup.TestSetup
import io.cucumber.datatable.DataTable
import io.cucumber.java.After
import io.cucumber.java.AfterAll
import io.cucumber.java.Before
import io.cucumber.java.BeforeAll
import io.cucumber.java.en.Given
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.connection.ConnectionSetup.Companion.loadConfigFromFile
import spaceEngineers.controller.extensions.waitForScreen
import testhelp.hideUndeclaredThrowableException


lateinit var testSetup: TestSetup

/*
SINGLE_COMPUTER_DEDICATED_DEV_KAREL    OFFLINE_STEAM
LOBBY_STEAM DS_STEAM
*/
val cfg = loadConfigFromFile("DS_STEAM.json")
val globalCm = ConnectionManager(cfg)

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
        testSetup = testSetupFactory(cfg, globalCm)
    }
    testSetup.beforeAll()
}

@AfterAll
fun afterAll() {
    testSetup.afterAll()
    //exitToMenuAndCloseEverything()
}

@Before
fun before() {
    testSetup.beforeScenario()
}

@After
fun after() {
    testSetup.afterScenario()
}


class ScenarioSetupSteps : AbstractMultiplayerSteps() {


    @Given("Scenario used is {string}.")
    fun scenario_used_is(scenarioId: String) = hideUndeclaredThrowableException {
        //loadScenario(scenarioId)
        ensureCharacterExists()
        observers {
            observer.observeNewBlocks()
        }
    }

    @Given("Scenario config:")
    fun scenario_config(dataTable: DataTable) = hideUndeclaredThrowableException {
        testSetup.scenarioConfig(dataTable.asMaps())
    }


}
