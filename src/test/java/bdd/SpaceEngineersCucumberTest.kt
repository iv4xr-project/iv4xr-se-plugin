package bdd

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.junit.Cucumber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import spaceEngineers.SeObservation
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.controller.ProprietaryJsonTcpCharacterController
import testhelp.TEST_AGENT

@RunWith(Cucumber::class)
class SpaceEngineersCucumberTest {
    lateinit var environment: ProprietaryJsonTcpCharacterController

    val observations: MutableList<SeObservation> = mutableListOf()

    @Before
    fun setup() {
        observations.clear()
    }

    @After
    fun cleanup() {
        observations.clear()
        if (this::environment.isInitialized) {
            environment.socketReaderWriter.close()
        }
    }

    @Given("I am connected to server.")
    fun i_am_connected_to_mock_server() {
        environment = ProprietaryJsonTcpCharacterController.localhost(agentId = TEST_AGENT)
    }

    @When("I request for blocks.")
    fun i_request_for_blocks() {
        environment.observe(ObservationArgs(ObservationMode.BLOCKS)).let { observations.add(it) }
    }

    @Then("I receive observation.")
    fun i_receive_observation() {
        assertTrue(observations.isNotEmpty())
    }

    @Then("I see {int} grid with {int} block.")
    fun i_see_grid_and_with_block(grids: Int, blocks: Int) {
        val observation = observations.last()
        assertEquals(grids, observation.grids.size)
        assertEquals(blocks, observation.grids[0].blocks.size)
    }

    @Then("Block with id {string} has {float} max integrity, {float} integrity and {float} build integrity.")
    fun block_with_id_blk_has_max_integrity_integrity_and_build_integrity(
            id: String, maxIntegrity: Float, integrity: Float, buildIntegrity: Float
    ) {
        val observation = observations.last()
        val block = observation.grids.flatMap { it.blocks }.find { it.id == id } ?: error("block $id not found")
        assertEquals(maxIntegrity, block.maxIntegrity)
        assertEquals(buildIntegrity, block.buildIntegrity)
        assertEquals(integrity, block.integrity)
    }
}