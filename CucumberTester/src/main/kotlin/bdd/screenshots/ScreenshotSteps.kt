package bdd.screenshots

import bdd.AbstractMultiplayerSteps
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import kotlinx.coroutines.delay
import bdd.connection.ConnectionManager
import spaceEngineers.controller.extensions.blockingMoveBackwardsByDistance
import spaceEngineers.controller.extensions.blockingMoveForwardByDistance
import spaceEngineers.controller.extensions.grindDownToPercentage
import spaceEngineers.controller.extensions.torchUpToPercentage
import spaceEngineers.model.Block
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.extensions.allBlocks
import java.io.File

class ScreenshotSteps(connectionManager: ConnectionManager) : AbstractMultiplayerSteps(connectionManager) {

    private val blockScreenshotInfoByType = mutableMapOf<String, Screenshots>()

    lateinit var outputDirectory: File

    @Given("Output directory is {string}.")
    fun outputDirectoryIs(outputDir: String) {
        outputDirectory = File(outputDir.replace("~", System.getProperty("user.home")))
        outputDirectory.mkdirs()
    }

    fun screenshot(block: Block, singleScreenshot: SingleScreenshot) = mainClient {
        blockScreenshotInfoByType.getOrPut(block.definitionId.type) {
            Screenshots(block)
        }.screenshots.add(singleScreenshot)
        val blockDir = File(outputDirectory, "${block.definitionId.type}").apply { mkdirs() }
        val screenshotFile = File(blockDir, singleScreenshot.filename)
        observer.takeScreenshot(screenshotFile.absolutePath)
    }

    fun observeLatestNewBlock(): Block = mainClient {
        observer.observeBlocks().allBlocks.firstOrNull { it.id == context.lastNewBlockId }
            ?: error("block with id ${context.lastNewBlockId} not found")
    }

    @Then("Character steps {float} units back and takes screenshot at initial integrity.")
    fun character_takes_screenshot_at_initial_integrity(distance: Float) {
        observeLatestNewBlock().let {
            moveBackScreenshotAndForward(it, SingleScreenshot(it, 1.0f), distance)
        }
    }

    @Then("Character grinds down to {double}% below each threshold, steps {float} units back and takes screenshot.")
    fun character_grinds_down_to_below_each_threshold_and_takes_screenshot(percentage: Double, distance: Float) =
        mainClient {
            val block = observeLatestNewBlock()
            val meta =
                definitions.blockDefinitions().first { it.definitionId.type == block.definitionId.type }
            meta.buildProgressModels.reversed().forEach { seBuildProgressModel ->
                delay(500)
                contextControllerWrapper.grindDownToPercentage((seBuildProgressModel.buildRatioUpperBound).toDouble() * 100.0 - percentage)
                delay(500)
                items.equip(ToolbarLocation(9, 0))
                delay(500)
                moveBackScreenshotAndForward(
                    block,
                    SingleScreenshot(observeLatestNewBlock(), seBuildProgressModel.buildRatioUpperBound),
                    distance
                )
            }
            delay(500)
        }

    @Then("Character welds up to {double}% above each threshold, steps {float} units back and takes screenshot.")
    fun character_welds_up_above_each_threshold_and_takes_screenshot(percentage: Double, distance: Float) =
        mainClient {
            val block = observeLatestNewBlock()
            val definition =
                definitions.blockDefinitions().first { it.definitionId.type == block.definitionId.type }
            definition.buildProgressModels.forEach { seBuildProgressModel ->
                Thread.sleep(500)
                contextControllerWrapper.torchUpToPercentage((seBuildProgressModel.buildRatioUpperBound).toDouble() * 100.0 + percentage)
                Thread.sleep(500)
                items.equip(ToolbarLocation(9, 0))
                Thread.sleep(500)
                moveBackScreenshotAndForward(
                    block,
                    SingleScreenshot(observeLatestNewBlock(), seBuildProgressModel.buildRatioUpperBound),
                    distance
                )
            }
            Thread.sleep(500)
        }

    @Then("Character saves metadata about each threshold and file names.")
    fun character_saves_metadata_about_each_threshold_and_file_names() {
        val block = observeLatestNewBlock()
        val blockDir = File(outputDirectory, block.definitionId.type).apply { mkdirs() }
        blockScreenshotInfoByType[block.definitionId.type]?.let { screenshots ->
            //val js = json.encodeToString(Screenshots.serializer(), screenshots)
            val js = ""
            File(
                blockDir,
                "screenshotInfo.json"
            ).writeText(js)

        }
    }

    private fun moveBackScreenshotAndForward(block: Block, singleScreenshot: SingleScreenshot, distance: Float = 2f) =
        mainClient {
            delay(500)
            val startPosition = observer.observe().position
            blockingMoveBackwardsByDistance(distance, startPosition = startPosition)
            screenshot(block, singleScreenshot)
            delay(500)
            val endPosition = observer.observe().position
            blockingMoveForwardByDistance(distance, startPosition = endPosition)
            delay(500)
        }

}
