package testhelp

import eu.iv4xr.framework.spatial.Vec3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import spaceEngineers.SeObservation

fun checkMockObservation(observation: SeObservation?) {
    assertNotNull(observation)
    assertEquals("Mock", observation?.agentID)
    assertEquals(Vec3(4.0f, 2.0f, 0.0f), observation?.position)
}