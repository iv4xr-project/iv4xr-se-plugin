package spaceEngineers.mock

import spaceEngineers.controller.WorldController
import testhelp.TEST_SCENARIO_1_ID
import testhelp.controller
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class SessionRequestTest {
    @Test
    fun sessionLoadTest() = controller {
        check(this is WorldController)
        loadScenario(TEST_SCENARIO_1_ID)
    }
}