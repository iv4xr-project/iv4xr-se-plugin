package bdd

import environments.closeIfCloseable
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.junit.Cucumber
import org.junit.runner.RunWith
import spaceEngineers.controller.*
import spaceEngineers.model.*
import testhelp.*
import java.io.File
import java.lang.Thread.sleep
import kotlin.test.assertEquals
import kotlin.test.assertTrue


fun runWhileConditionUntilTimeout(
    timeoutMs: Int = 20000,
    conditionBlock: () -> Boolean
) {
    val initialTime = System.currentTimeMillis()
    while (conditionBlock()) {
        check(System.currentTimeMillis() - initialTime < timeoutMs) {
            "Timeout after ${timeoutMs}ms"
        }
    }
}

@RunWith(Cucumber::class)
class SpaceEngineersCucumberTest {
    lateinit var environment: ContextControllerWrapper

    val context by lazy { environment.context }

    @Before
    fun setup() {
    }

    @After
    fun cleanup() {
        if (this::environment.isInitialized) {
            environment.spaceEngineers.closeIfCloseable()
            environment.closeIfCloseable()
        }
    }

    @Given("I am using mock data source.")
    fun i_am_connected_to_mock_server() {
        environment =
            ContextControllerWrapper(
                spaceEngineers = OldProtocolSpaceEngineers(
                    ProprietaryJsonTcpCharacterController.mock(
                        agentId = TEST_AGENT,
                        lineToReturn = TEST_MOCK_RESPONSE_LINE
                    )
                )
            )
    }

    @Given("I am connected to real game.")
    fun i_am_connected_to_real_game() {
        environment =
            ContextControllerWrapper(
                spaceEngineers = OldProtocolSpaceEngineers(
                    ProprietaryJsonTcpCharacterController.localhost(agentId = TEST_AGENT)
                )
            )
    }

    @Given("I am connected to real game using json-rpc.")
    fun i_am_connected_to_real_game_using_json_rpc() {

        environment =
            ContextControllerWrapper(
                spaceEngineers = JsonRpcCharacterController.localhost(agentId = TEST_AGENT)
            )
    }

    @Given("Toolbar has mapping:")
    fun toolbar_has_mapping(dataTable: List<Map<String, String>>) {
        context.updateToolbarLocation(dataTable)
    }

    @Given("Grinder is in slot {int}, page {int}.")
    fun grinder_is_at(slot: Int, page: Int) {
        context.grinderLocation = ToolbarLocation(slot = slot, page = page)
    }

    @Given("Torch is in slot {int}, page {int}.")
    fun torch_is_at(slot: Int, page: Int) {
        context.torchLocation = ToolbarLocation(slot = slot, page = page)
    }

    @Given("I load scenario {string}.")
    fun i_load_scenario(scenarioId: String) {
        environment?.let { wrapper ->
            wrapper.session.loadScenario(File("$SCENARIO_DIR$scenarioId").absolutePath)
        }
        // All blocks are new for the first request.
        environment.observer.observeNewBlocks().let {
            assertTrue(it.allBlocks.isNotEmpty())
        }
        environment.observer.observeNewBlocks().let {
            assertTrue(it.allBlocks.isEmpty())
        }
    }

    @When("Character sets toolbar slot {int}, page {int} to {string}.")
    fun character_sets_toolbar_slot_page_to(slot: Int, page: Int, itemName: String) {
        val location = ToolbarLocation(slot, page)

        environment.setToolbarItem(location, itemName)

        // Note: maybe we should use the toolbar mapping instead, and have some way to define exact names of the tools
        if (itemName.startsWith("Welder")) {
            context.torchLocation = location
        } else if (itemName.startsWith("AngleGrinder")) {
            context.grinderLocation = location
        }
    }

    @When("I request for blocks.")
    fun i_request_for_blocks() {
        environment.observer.observeBlocks()
    }

    @When("I observe.")
    fun i_observe() {
        environment.observer.observe()
    }

    @Then("Character is at \\({double}, ?{double}, ?{double}).")
    fun i_see_character_at_x_y_z(x: Double, y: Double, z: Double) {
        val position = Vec3(x, y, z)
        context.observationHistory.last().let { observation ->
            assertVecEquals(position, observation.position, diff = 0.1f)
        }
    }

    @Then("Character forward orientation is \\({double}, {double}, {double}).")
    fun character_is_facing(x: Double, y: Double, z: Double) {
        val position = Vec3(x, y, z)
        context.observationHistory.last().let { observation ->
            assertVecEquals(position, observation.orientationForward, diff = 0.1f)
        }
    }

    @When("Character moves forward for {int} units.")
    fun character_moves_forward_for_units(units: Int) {
        environment.character.blockingMoveForwardByDistance(
            distance = units.toFloat(),
            startPosition = environment.observer.observe().position
        ).let { context.addToHistory(it) }
    }

    @Then("Character is {int} units away from starting location.")
    fun character_is_units_away_from_starting_location(units: Int) {
        assertFloatEquals(
            units.toFloat(),
            context.observationHistory.first().position.distanceTo(context.observationHistory.last().position)
        )
    }

    private fun observeBlocks(): List<SeBlock> {
        return environment.observer.observeBlocks().allBlocks
    }

    private fun blockToGrind(): SeBlock {
        return observeBlocks().first { it.id == context.lastNewBlockId }
    }

    @When("Character grinds to {double} integrity.")
    fun character_grinds_to_integrity(integrity: Double) {
        environment.items.equip(context.grinderLocation!!)
        sleep(500)
        environment.items.startUsingTool()
        runWhileConditionUntilTimeout {
            blockToGrind().integrity > integrity
        }
        environment.items.endUsingTool()
    }

    @When("Character grinds to {double}% integrity.")
    fun character_grinds_until_to_integrity_percentage(percentage: Double) {
        val integrity = blockToGrind().maxIntegrity * percentage / 100.0
        environment.items.equip(context.grinderLocation!!)
        sleep(500)
        environment.items.startUsingTool()
        runWhileConditionUntilTimeout {
            blockToGrind().integrity > integrity
        }
        environment.items.endUsingTool()
    }

    @When("Character torches block back up to max integrity.")
    fun character_torches_block_back_up_to_max_integrity() {
        val maxIntegrity = blockToGrind().maxIntegrity
        environment.items.equip(context.torchLocation!!)
        sleep(500)
        environment.items.startUsingTool()
        runWhileConditionUntilTimeout {
            blockToGrind().integrity < maxIntegrity
        }
        environment.items.endUsingTool()
    }


    @Then("I receive observation.")
    fun i_receive_observation() {
        assertTrue(context.observationHistory.isNotEmpty())
    }

    @Then("I see {int} grid with {int} block.")
    fun i_see_grid_and_with_block(grids: Int, blocks: Int) {
        val observation = context.observationHistory.last()
        assertEquals(grids, observation.grids?.size)
        assertEquals(blocks, observation.grids?.first()?.blocks?.size)
    }

    @When("Character selects block {string} and places it.")
    fun character_places_selects_block_and_places_it(blockType: String) {
        val toolbarLocation: ToolbarLocation = context.blockToolbarLocation(blockType)
        environment.items.equip(toolbarLocation)
        environment.items.place()
    }

    @Then("I see no block of type {string}.")
    fun i_see_no_block_of_type(string: String) {
        val observation = environment.observer.observeBlocks()
        assertTrue(
            observation.allBlocks
                .none { it.blockType == string }
        )
    }

    @Then("I can see {int} new block\\(s) with data:")
    fun i_can_see_new_block_with_data(blockCount: Int, data: List<Map<String, String>>) {
        val observation = environment.observer.observeNewBlocks()
        val allBlocks = observation.allBlocks
        assertEquals(
            blockCount,
            allBlocks.size,
            "Expected to see $blockCount blocks, not ${allBlocks.size} ${allBlocks.map { it.blockType }.toSet()}."
        )
        assertEquals(allBlocks.size, data.size)
        data.forEachIndexed { index, row ->
            val block = allBlocks[index]
            context.lastNewBlockId = block.id
            row["blockType"]?.let {
                assertEquals(it, block.blockType)
            }
            row["integrity"]?.let {
                assertEquals(it.toFloat(), block.integrity)
            }
            row["maxIntegrity"]?.let {
                assertEquals(it.toFloat(), block.maxIntegrity)
            }
            row["buildIntegrity"]?.let {
                assertEquals(it.toFloat(), block.buildIntegrity)
            }
        }
    }

    @Then("Block with id {string} has {float} max integrity, {float} integrity and {float} build integrity.")
    fun block_with_id_blk_has_max_integrity_integrity_and_build_integrity(
        id: String, maxIntegrity: Float, integrity: Float, buildIntegrity: Float
    ) {
        val observation = context.observationHistory.last()
        val block = observation.grids.flatMap { it.blocks }.find { it.id == id } ?: error("block $id not found")
        assertEquals(maxIntegrity, block.maxIntegrity)
        assertEquals(buildIntegrity, block.buildIntegrity)
        assertEquals(integrity, block.integrity)
    }
}