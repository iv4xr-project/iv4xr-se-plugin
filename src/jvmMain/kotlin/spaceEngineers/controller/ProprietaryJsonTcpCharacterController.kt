package spaceEngineers.controller

import environments.SocketReaderWriter
import spaceEngineers.SeRequest
import spaceEngineers.commands.*
import spaceEngineers.model.SeObservation
import spaceEngineers.transport.AlwaysReturnSameLineReaderWriter
import spaceEngineers.transport.GsonReaderWriter
import spaceEngineers.transport.StringLineReaderWriter

class ProprietaryJsonTcpCharacterController(val agentId: String, val gsonReaderWriter: GsonReaderWriter) :
    CharacterController, AutoCloseable, WorldController {

    private fun <T> SeRequest<T>.process(): T {
        return processRequest(this)
    }

    fun <T> processRequest(request: SeRequest<T>): T {
        return gsonReaderWriter.processRequest(request)
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

    override fun loadScenario(scenarioPath: String) {
        SeRequest.session(SeSessionCommand.load(scenarioPath)).process()
    }

    companion object {
        fun localhost(
            agentId: String,
            stringLineReaderWriter: StringLineReaderWriter = SocketReaderWriter(),
            gsonReaderWriter: GsonReaderWriter = GsonReaderWriter(stringLineReaderWriter = stringLineReaderWriter)
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