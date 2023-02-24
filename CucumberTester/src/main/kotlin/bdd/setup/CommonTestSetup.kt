package bdd.setup

import io.cucumber.java.Scenario
import bdd.connection.ConnectionManager

class CommonTestSetup(override val connectionManager: ConnectionManager) : TestSetup {
    override fun beforeAll() {

    }

    override fun beforeScenario(scenario: Scenario) {
        TODO("Not yet implemented")
    }

    override fun afterScenario(scenario: Scenario) {
        TODO("Not yet implemented")
    }

    override fun afterAll() {
        TODO("Not yet implemented")
    }

    override suspend fun scenarioConfig(dataTable: List<Map<String, String>>) {
        TODO("Not yet implemented")
    }
}
