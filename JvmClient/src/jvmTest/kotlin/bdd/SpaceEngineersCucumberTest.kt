package bdd

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.junit.Cucumber
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcSpaceEngineers
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.controller.extensions.*
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.Block
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.closeIfCloseable
import testhelp.TEST_AGENT
import testhelp.assertFloatEquals
import testhelp.assertVecEquals
import java.io.File
import java.lang.Thread.sleep
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

    @Given("I am connected to real game using json-rpc.")
    fun i_am_connected_to_real_game_using_json_rpc() = runBlocking {

        environment =
            ContextControllerWrapper(
                spaceEngineers = JsonRpcSpaceEngineersBuilder.localhost(agentId = TEST_AGENT)
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
        environment.session.loadFromTestResources(scenarioId)
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
        val position = Vec3F(x, y, z)
        context.observationHistory.last().let { observation ->
            assertVecEquals(position, observation.character.position, diff = 0.1f)
        }
    }

    @Then("Character forward orientation is \\({double}, {double}, {double}).")
    fun character_is_facing(x: Double, y: Double, z: Double) {
        val position = Vec3F(x, y, z)
        context.observationHistory.last().let { observation ->
            assertVecEquals(position, observation.character.orientationForward, diff = 0.1f)
        }
    }

    @When("Character moves forward for {float} units.")
    fun character_moves_forward_for_units(units: Float) = runBlocking {
        environment.blockingMoveForwardByDistance(
            distance = units,
            startPosition = environment.observer.observe().position
        )
        environment.observer.observe()
    }

    @Then("Character is {int} units away from starting location.")
    fun character_is_units_away_from_starting_location(units: Int) {
        assertFloatEquals(
            units.toFloat(),
            context.observationHistory.first().character.position.distanceTo(context.observationHistory.last().character.position)
        )
    }

    private fun observeBlocks(): List<Block> {
        return environment.observer.observeBlocks().allBlocks
    }

    private fun blockToGrind(): Block {
        return observeBlocks().first { it.id == context.lastNewBlockId }
    }

    private suspend fun grindDownToPercentage(percentage: Double) {
        environment.grindDownToPercentage(context.lastNewBlock!!, percentage)
    }

    private suspend fun torchUpToPercentage(percentage: Double) {
        environment.torchUpToPercentage(context.lastNewBlock!!, percentage, torchLocation = context.torchLocation!!)
    }

    @When("Character grinds to {double} integrity.")
    fun character_grinds_to_integrity(integrity: Double) = runBlocking {
        val percentage = integrity / blockToGrind().maxIntegrity * 100.0
        grindDownToPercentage(percentage)
    }

    @When("Character grinds to {double}% integrity.")
    fun character_grinds_until_to_integrity_percentage(percentage: Double) = runBlocking {
        grindDownToPercentage(percentage)
    }

    @When("Character torches block back up to max integrity.")
    fun character_torches_block_back_up_to_max_integrity() = runBlocking {
        environment.torchBackToMax(context.lastNewBlock!!)
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
        val toolbarLocation =
            environment.items.getToolbar().findLocation(blockType) ?: error("cannot find $blockType in toolbar")
        environment.items.setToolbarItem(blockType, toolbarLocation);
        environment.items.equip(toolbarLocation)
        sleep(150)
        environment.blocks.place()
        environment.items.equip(ToolbarLocation(9, 0))
    }

    @Then("I see no block of type {string}.")
    fun i_see_no_block_of_type(string: String) {
        val observation = environment.observer.observeBlocks()
        assertTrue(
            observation.allBlocks
                .none { it.definitionId.type == string }
        )
    }

    @Then("I can see {int} new block\\(s) with data:")
    fun i_can_see_new_block_with_data(blockCount: Int, data: List<Map<String, String>>) {
        val observation = environment.observer.observeNewBlocks()
        val allBlocks = observation.allBlocks
        assertEquals(
            blockCount,
            allBlocks.size,
            "Expected to see $blockCount blocks, not ${allBlocks.size} ${
                allBlocks.map { it.definitionId.type }.toSet()
            }."
        )
        assertEquals(allBlocks.size, data.size)
        data.forEachIndexed { index, row ->
            val block = allBlocks[index]
            context.lastNewBlock = block
            row["blockType"]?.let {
                assertEquals(it, block.definitionId.type)
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
        constructor(block: Block) : this(block.definitionId.type, block.maxIntegrity)
    }

    data class SingleScreenshot(
        val filename: String,
        val integrity: Float,
        val buildRatioUpperBound: Float
    ) {
        constructor(block: Block, buildRatioUpperBound: Float) : this(
            filename = "${block.definitionId.type}_${block.integrity}.png",
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

    fun moveBackScreenshotAndForward(block: Block, singleScreenshot: SingleScreenshot, distance: Float = 2f) =
        runBlocking {
            sleep(500)
            val startPosition = environment.observer.observe().position
            environment.blockingMoveBackwardsByDistance(distance, startPosition = startPosition)
            screenshot(block, singleScreenshot)
            sleep(500)
            val endPosition = environment.observer.observe().position
            environment.blockingMoveForwardByDistance(distance, startPosition = endPosition)
            sleep(500)
        }

    fun screenshot(block: Block, singleScreenshot: SingleScreenshot) {
        blockScreenshotInfoByType.getOrPut(block.definitionId.type) {
            Screenshots(block)
        }.screenshots.add(singleScreenshot)
        val blockDir = File(outputDirectory, "${block.definitionId.type}").apply { mkdirs() }
        val screenshotFile = File(blockDir, singleScreenshot.filename)
        environment.observer.takeScreenshot(screenshotFile.absolutePath)
    }

    fun observeLatestNewBlock(): Block {
        return environment.observer.observeBlocks().allBlocks.firstOrNull { it.id == context.lastNewBlockId }
            ?: error("block with id ${context.lastNewBlockId} not found")
    }

    @Then("Character grinds down to {double}% below each threshold, steps {float} units back and takes screenshot.")
    fun character_grinds_down_to_below_each_threshold_and_takes_screenshot(percentage: Double, distance: Float) =
        runBlocking {
            val block = observeLatestNewBlock()
            val meta =
                environment.definitions.blockDefinitions().first { it.definitionId.type == block.definitionId.type }
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
    fun character_welds_up_above_each_threshold_and_takes_screenshot(percentage: Double, distance: Float) =
        runBlocking {
            val block = observeLatestNewBlock()
            val definition =
                environment.definitions.blockDefinitions().first { it.definitionId.type == block.definitionId.type }
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
        val cw = environment.spaceEngineers as JsonRpcSpaceEngineers
        //val meta = cw.blockDefinitions().first { it.definitionId.type == block.definitionId.type }
        val blockDir = File(outputDirectory, "${block.definitionId.type}").apply { mkdirs() }
        //File(blockDir, "blockDefinition.json").writeText(cw.gsonReaderWriter.gson.toJson(meta))
        File(
            blockDir,
            "screenshotInfo.json"
        ).writeText(SocketReaderWriter.SPACE_ENG_GSON.toJson(blockScreenshotInfoByType[block.definitionId.type]))
    }

    @When("Character turns on jetpack.")
    fun character_turns_on_jetpack() {
        environment.character.turnOnJetpack()
    }

    @Then("jetpack is on.")
    fun jetpack_is_on() {
        assertTrue(environment.observer.observe().jetpackRunning)
    }

    @Then("Character uses.")
    fun character_uses() {
        environment.character.use()
    }

    @Then("jetpack is off.")
    fun jetpack_is_off() {
        assertFalse(environment.observer.observe().jetpackRunning)
    }

    @Then("Character waits {int} seconds.")
    fun character_waits_seconds(seconds: Int) {
        sleep(seconds * 1000L)
    }


    @When("Character moves up for {int} ticks.")
    fun character_moves_up_for_ticks(ticks: Int) {
        environment.character.moveAndRotate(Vec3F.UP, ticks = ticks)
    }

    @When("Character moves forward for {int} ticks.")
    fun character_moves_forward_for_ticks(ticks: Int) {
        environment.character.moveAndRotate(Vec3F.FORWARD, ticks = ticks)
    }

    @Then("Character speed is {int} m\\/s.")
    fun character_speed_is_100m_s(speed: Int) {
        assertEquals(speed.toFloat(), environment.observer.observe().velocity.length(), 0.0001f)
    }

    @Then("Observed grid mass is {double}.")
    fun observed_grid_mass_is(mass: Double) {
        assertEquals(mass.toFloat(), environment.observer.observeBlocks().grids.first().mass)
    }

    @When("Block type {string} has mass {double}.")
    fun block_type_has_mass(type: String, mass: Double) {
        val blockDefinition = environment.definitions.blockDefinitions().first {
            it.definitionId.type == type
        }
        assertEquals(mass.toFloat(), blockDefinition.mass)
    }
}
