package spaceEngineers.controller

import environments.SocketReaderWriter
import spaceEngineers.SeRequest
import spaceEngineers.commands.*
import spaceEngineers.model.SeObservation
import spaceEngineers.transport.AlwaysReturnSameLineReaderWriter
import spaceEngineers.transport.GsonReaderWriter

class ProprietaryJsonTcpCharacterController(val agentId: String, val gsonReaderWriter: GsonReaderWriter) :
    CharacterController, AutoCloseable {

    private fun <T> SeRequest<T>.process(): T {
        return gsonReaderWriter.processRequest(this)
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
        fun localhost(
            agentId: String,
            gsonReaderWriter: GsonReaderWriter = GsonReaderWriter(stringLineReaderWriter = SocketReaderWriter())
        ): ProprietaryJsonTcpCharacterController {
            return ProprietaryJsonTcpCharacterController(
                agentId = agentId,
                gsonReaderWriter = gsonReaderWriter
            )
        }

        fun mock(
            agentId: String,
            lineToReturn: String
        ): ProprietaryJsonTcpCharacterController {
            return ProprietaryJsonTcpCharacterController(
                agentId = agentId, gsonReaderWriter = GsonReaderWriter(
                    stringLineReaderWriter = AlwaysReturnSameLineReaderWriter(response = lineToReturn)
                )
            )
        }
    }

    override fun close() {
        gsonReaderWriter.close()
    }
}