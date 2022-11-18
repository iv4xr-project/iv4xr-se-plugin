package bdd

import spaceEngineers.config.cfg
import spaceEngineers.config.globalCm
import bdd.setup.MedbayDSSetup
import bdd.setup.MedbayLobbySetup
import bdd.setup.TestSetup
import io.cucumber.datatable.DataTable
import io.cucumber.java.*
import io.cucumber.java.en.Given
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import testhelp.hideUndeclaredThrowableException


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
fun before(scenario: Scenario) {
    testSetup.beforeScenario(scenario)
}

@After
fun after(scenario: Scenario) {
    testSetup.afterScenario(scenario)
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
