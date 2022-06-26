package bdd.setup

import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import kotlinx.coroutines.delay
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.connection.AppType
import spaceEngineers.controller.connection.ConnectionManager
import testhelp.hideUndeclaredThrowableException

interface TestSetup {
    val connectionManager: ConnectionManager

    fun beforeAll()
    fun beforeScenario()
    fun afterScenario()
    fun afterAll()


    suspend fun scenarioConfig(dataTable: List<Map<String, String>>)
}

abstract class AbstractTestSetup(
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config)
) : TestSetup, RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase, ConnectionManagerUser {

}
