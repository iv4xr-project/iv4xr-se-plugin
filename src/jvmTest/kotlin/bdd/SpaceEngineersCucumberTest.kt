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
import testhelp.TEST_AGENT
import testhelp.TEST_MOCK_RESPONSE_LINE
import testhelp.assertFloatEquals
import testhelp.assertVecEquals
import java.io.File
import java.lang.Thread.sleep
import kotlin.test.assertEquals
import kotlin.test.assertTrue


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
            wrapper.session.loadFromTestResources(scenarioId)
        }
        environment.observer.observe()
        // All blocks are new for the first request.
        environment.observer.observeNewBlocks()
    }

    @When("Character sets toolbar slot {int}, page {int} to {string}.")
    fun character_sets_toolbar_slot_page_to(slot: Int, page: Int, itemName: String) {
        val location = ToolbarLocation(slot, page)

        environment.items.setToolbarItem(itemName, location)

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

    @When("Character moves forward for {float} units.")
    fun character_moves_forward_for_units(units: Float) {
        environment.character.blockingMoveForwardByDistance(
            distance = units,
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

    private fun observeBlocks(): List<SlimBlock> {
        return environment.observer.observeBlocks().allBlocks
    }

    private fun blockToGrind(): SlimBlock {
        return observeBlocks().first { it.id == context.lastNewBlockId }
    }

    private fun grindDownToPercentage(percentage: Double) {
        environment.grindDownToPercentage(context.lastNewBlockId!!, percentage)
    }

    private fun torchUpToPercentage(percentage: Double) {
        environment.torchUpToPercentage(context.lastNewBlockId!!, percentage)
    }

    @When("Character grinds to {double} integrity.")
    fun character_grinds_to_integrity(integrity: Double) {
        val percentage = integrity / blockToGrind().maxIntegrity * 100.0
        grindDownToPercentage(percentage)
    }

    @When("Character grinds to {double}% integrity.")
    fun character_grinds_until_to_integrity_percentage(percentage: Double) {
        grindDownToPercentage(percentage)
    }

    @When("Character torches block back up to max integrity.")
    fun character_torches_block_back_up_to_max_integrity() {
        environment.torchBackToMax(context.lastNewBlockId!!)
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
        val toolbarLocation = environment.items.getToolbar().findLocation(blockType) ?: error("cannot find $blockType in toolbar")
        environment.items.setToolbarItem(blockType, toolbarLocation);
        environment.items.equip(toolbarLocation)
        sleep(150)
        environment.items.place()
        environment.items.equip(ToolbarLocation(9, 0))
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


    lateinit var outputDirectory: File

    private val blockScreenshotInfoByType = mutableMapOf<String, Screenshots>()

    data class Screenshots(
        val blockType: String,
        val maxIntegrity: Float,
        val screenshots: MutableList<SingleScreenshot> = mutableListOf()
    ) {
        constructor(block: SlimBlock) : this(block.blockType, block.maxIntegrity)
    }

    data class SingleScreenshot(
        val filename: String,
        val integrity: Float,
        val buildRatioUpperBound: Float
    ) {
        constructor(block: SlimBlock, buildRatioUpperBound: Float) : this(
            filename = "${block.blockType}_${block.integrity}.png",
            integrity = block.integrity,
            buildRatioUpperBound = buildRatioUpperBound
        )
    }

    @Given("Output directory is {string}.")
    fun outputDirectoryIs(outputDir: String) {
        outputDirectory = File(outputDir.replace("~", System.getProperty("user.home")))
        outputDirectory.mkdirs()
    }

    @Then("Character steps {float} units back and takes screenshot at initial integrity.")
    fun character_takes_screenshot_at_initial_integrity(distance: Float) {
        observeLatestNewBlock().let {
            moveBackScreenshotAndForward(it, SingleScreenshot(it, 1.0f), distance)
        }
    }

    fun moveBackScreenshotAndForward(block: SlimBlock, singleScreenshot: SingleScreenshot, distance: Float = 2f) {
        sleep(500)
        val startPosition = environment.observer.observe().position
        environment.character.blockingMoveBackwardsByDistance(distance, startPosition = startPosition)
        screenshot(block, singleScreenshot)
        sleep(500)
        val endPosition = environment.observer.observe().position
        environment.character.blockingMoveForwardByDistance(distance, startPosition = endPosition)
        sleep(500)
    }

    fun screenshot(block: SlimBlock, singleScreenshot: SingleScreenshot) {
        blockScreenshotInfoByType.getOrPut(block.blockType) {
            Screenshots(block)
        }.screenshots.add(singleScreenshot)
        val blockDir = File(outputDirectory, "${block.blockType}").apply { mkdirs() }
        val screenshotFile = File(blockDir, singleScreenshot.filename)
        environment.observer.takeScreenshot(screenshotFile.absolutePath)
    }

    fun observeLatestNewBlock(): SlimBlock {
        return environment.observer.observeBlocks().allBlocks.first { it.id == context.lastNewBlockId }
    }

    @Then("Character grinds down to {double}% below each threshold, steps {float} units back and takes screenshot.")
    fun character_grinds_down_to_below_each_threshold_and_takes_screenshot(percentage: Double, distance: Float) {
        val block = observeLatestNewBlock()
        val meta = environment.definitions.blockDefinitions().first { it.blockType == block.blockType }
        meta.buildProgressModels.reversed().forEach { seBuildProgressModel ->
            sleep(500)
            grindDownToPercentage((seBuildProgressModel.buildRatioUpperBound).toDouble() * 100.0 - percentage)
            sleep(500)
            environment.items.equip(ToolbarLocation(9, 0))
            sleep(500)
            moveBackScreenshotAndForward(
                block,
                SingleScreenshot(observeLatestNewBlock(), seBuildProgressModel.buildRatioUpperBound),
                distance
            )
        }
        sleep(500)
    }

    @Then("Character welds up to {double}% above each threshold, steps {float} units back and takes screenshot.")
    fun character_welds_up_above_each_threshold_and_takes_screenshot(percentage: Double, distance: Float) {
        val block = observeLatestNewBlock()
        val definition = environment.definitions.blockDefinitions().first { it.blockType == block.blockType }
        definition.buildProgressModels.forEach { seBuildProgressModel ->
            sleep(500)
            torchUpToPercentage((seBuildProgressModel.buildRatioUpperBound).toDouble() * 100.0 + percentage)
            sleep(500)
            environment.items.equip(ToolbarLocation(9, 0))
            sleep(500)
            moveBackScreenshotAndForward(
                block,
                SingleScreenshot(observeLatestNewBlock(), seBuildProgressModel.buildRatioUpperBound),
                distance
            )
        }
        sleep(500)
    }

    @Then("Character saves metadata about each threshold and file names.")
    fun character_saves_metadata_about_each_threshold_and_file_names() {
        val block = observeLatestNewBlock()
        val cw = environment.spaceEngineers as JsonRpcCharacterController
        //val meta = cw.blockDefinitions().first { it.blockType == block.blockType }
        val blockDir = File(outputDirectory, "${block.blockType}").apply { mkdirs() }
        //File(blockDir, "blockDefinition.json").writeText(cw.gsonReaderWriter.gson.toJson(meta))
        File(
            blockDir,
            "screenshotInfo.json"
        ).writeText(cw.gsonReaderWriter.gson.toJson(blockScreenshotInfoByType[block.blockType]))
    }

}
