package bdd

import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.CharacterMovement
import spaceEngineers.model.Vec3F
import spaceEngineers.transport.SocketReaderWriter
import kotlin.test.assertEquals


class MultiplayerSteps : AbstractMultiplayerSteps() {

    @Given("Dedicated server is at {string}, plugin running at port {string}.")
    fun dedicated_server_is_at_plugin_running_at_port(address: String, pluginPort: String) {
        serverAddress = "$address:27016"
        addConnection(SERVER, address, pluginPort.toInt())
    }

    @Given("Client {string} is at {string}, plugin running at port {string}.")
    fun client_is_at_plugin_running_at_port(name: String, address: String, port: String) {
        addConnection(name, address, port.toInt())
    }


    @Given("Client {string} connects to the server.")
    fun client_starts_connects_to_the_server(name: String) = connection(name) {
        session.connect(serverAddress)
    }

    @Given("Server loads scenario {string}.")
    fun server_loads_scenario(scenarioId: String) {
        server.session.loadFromTestResources(scenarioId)
    }

    @Given("Character {string} is standing as seen by server.")
    fun character_is_standing_as_seen_by_server(name: String) {
        val characterId = getCharacterIdByName(name)
        server.admin.observer.observeCharacters().first { it.id == characterId }.let {
            assertEquals(CharacterMovement.standing, it.movement.value.toInt())
        }
    }

    @Given("Character {string} is standing as seen by itself.")
    fun character_is_standing_as_seen_by_itself(name: String) = connection(name) {
        assertEquals(CharacterMovement.standing, observer.observe().movement.value.toInt())
    }

    @When("Character {string} crouches.")
    fun character_crouches(name: String) = connection(name) {
        character.moveAndRotate(Vec3F.DOWN, ticks = 0)
    }

    @Then("Character {string} is crouching as seen by server.")
    fun character_is_crouching_as_seen_by_server(name: String) {
        val characterId = getCharacterIdByName(name)
        server.admin.observer.observeCharacters().first { it.id == characterId }.let {
            assertEquals(CharacterMovement.crouching, it.movement.value.toInt())
        }
    }

    @Then("Character {string} is crouching as seen by itself.")
    fun character_is_crouching_as_seen_by_itself(name: String) = connection(name) {
        assertEquals(CharacterMovement.crouching, observer.observe().movement.value.toInt())
    }

    private fun addConnection(name: String, address: String, port: Int): ContextControllerWrapper {
        check(name !in connections) {
            "$name already in connections!"
        }
        val se = ContextControllerWrapper(
            spaceEngineers = JsonRpcSpaceEngineersBuilder.fromStringLineReaderWriter(
                agentId = name,
                stringLineReaderWriter = SocketReaderWriter(host = address, port = port)
            )
        )
        connections[name] = se
        return se
    }

    companion object {
        const val SERVER = "SERVER"
    }

}
