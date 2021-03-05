package spaceEngineers.controller

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import environments.SocketReaderWriter
import environments.processRequest
import spaceEngineers.model.SeObservation
import spaceEngineers.SeRequest
import spaceEngineers.commands.*
import java.lang.reflect.Modifier

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
        const val DEFAULT_HOSTNAME = "localhost"

        const val DEFAULT_PORT = 9678

        val SPACE_ENG_GSON: Gson = GsonBuilder()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create()


        fun localhost(agentId: String): ProprietaryJsonTcpCharacterController {
            return ProprietaryJsonTcpCharacterController(
                agentId = agentId,
                socketReaderWriter = SocketReaderWriter(
                    host = DEFAULT_HOSTNAME,
                    port = DEFAULT_PORT,
                    gson = SPACE_ENG_GSON
                )
            )
        }
    }
}