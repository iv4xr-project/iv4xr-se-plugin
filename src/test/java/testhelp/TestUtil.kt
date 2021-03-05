package testhelp

import eu.iv4xr.framework.spatial.Vec3
import org.junit.jupiter.api.Assertions
import spaceEngineers.*
import spaceEngineers.controller.ProprietaryJsonTcpCharacterController

const val TEST_AGENT = "you\nx"

fun controller(
    agentId: String = TEST_AGENT,
    characterController: ProprietaryJsonTcpCharacterController = ProprietaryJsonTcpCharacterController.localhost(agentId = agentId),
    block: ProprietaryJsonTcpCharacterController.() -> Unit
) {
    try {
        block(characterController)
    } finally {
        characterController.socketReaderWriter.close()
    }
}