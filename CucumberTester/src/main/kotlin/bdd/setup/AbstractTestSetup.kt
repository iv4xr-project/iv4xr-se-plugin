package bdd.setup

import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import io.cucumber.java.Scenario
import spaceEngineers.controller.connection.ConnectionManager

interface TestSetup {
    val connectionManager: ConnectionManager

    fun beforeAll()
    fun beforeScenario(scenario: Scenario)
    fun afterScenario(scenario: Scenario)
    fun afterAll()


    suspend fun scenarioConfig(dataTable: List<Map<String, String>>)
}

abstract class AbstractTestSetup(
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config)
) : TestSetup, RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase, ConnectionManagerUser {

}
