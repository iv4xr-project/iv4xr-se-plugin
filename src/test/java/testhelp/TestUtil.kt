package testhelp

import eu.iv4xr.framework.spatial.Vec3
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import spaceEngineers.SeObservation
import spaceEngineers.SeRequest
import spaceEngineers.SpaceEngEnvironment

const val TEST_AGENT = "you"

fun environment(
    environment: SpaceEngEnvironment = SpaceEngEnvironment.localhost(),
    block: SpaceEngEnvironment.() -> Unit
) {
    try {
        block(environment)
    } finally {
        environment.close()
    }
}

fun checkMockObservation(observation: SeObservation?) {
    Assertions.assertNotNull(observation)
    Assertions.assertEquals("Mock", observation?.agentID)
    Assertions.assertEquals(Vec3(4.0f, 2.0f, 0.0f), observation?.position)
}