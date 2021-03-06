package spaceEngineers.controller

import environments.SocketReaderWriter
import spaceEngineers.SeRequest
import spaceEngineers.commands.*
import spaceEngineers.model.SeObservation

class ProprietaryJsonTcpCharacterController(val agentId: String, val socketReaderWriter: SocketReaderWriter) :
    CharacterController {

    private fun <T> SeRequest<T>.process(): T {
        return socketReaderWriter.processRequest(this)
    }

    override fun moveAndRotate(movementArgs: MovementArgs): SeObservation {
        return SeRequest.command(SeAgentCommand.moveAndRotate(agentId, movementArgs)).process()
    }

    override fun moveTowards(moveTowardsArgs: MoveTowardsArgs): SeObservation {
        return SeRequest.command(SeAgentCommand.moveTowardCommand(agentId, moveTowardsArgs)).process()
    }

    override fun observe(observationArgs: ObservationArgs): SeObservation {
        return SeRequest.command(SeAgentCommand.observe(agentId, observationArgs)).process()
    }

    override fun interact(interactionArgs: InteractionArgs): SeObservation {
        return SeRequest.command(SeAgentCommand.interact(agentId, interactionArgs)).process()
    }

    companion object {
        fun localhost(agentId: String): ProprietaryJsonTcpCharacterController {
            return ProprietaryJsonTcpCharacterController(
                agentId = agentId,
                socketReaderWriter = SocketReaderWriter()
            )
        }
    }
}