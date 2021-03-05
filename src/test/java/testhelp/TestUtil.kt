package testhelp

import spaceEngineers.controller.ProprietaryJsonTcpCharacterController

const val TEST_AGENT = "you"

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