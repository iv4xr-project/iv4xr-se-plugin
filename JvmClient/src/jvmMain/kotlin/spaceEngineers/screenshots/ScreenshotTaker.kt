package spaceEngineers.screenshots

import com.google.gson.Gson
import spaceEngineers.controller.Character.Companion.DISTANCE_CENTER_CAMERA
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.BuildProgressModel
import spaceEngineers.model.CubeSize
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.centerPosition
import spaceEngineers.screenshots.NamedOrientations.*
import spaceEngineers.transport.SocketReaderWriter
import java.io.File
import java.lang.Thread.sleep

fun main() {
    ScreenshotTaker(
        se = JsonRpcSpaceEngineersBuilder.localhost("agentId"),
        outputDirectory = File(System.getProperty("user.home"), "screenshots_v2")
    ).run()
}

class ScreenshotTaker(
    val se: SpaceEngineers,
    val orientations: List<NamedOrientations> = listOf(
        FORWARD_UP,
        BACKWARD_UP,
        LEFT_UP,
        RIGHT_UP,
        UP_FORWARD,
        DOWN_BACKWARD,
    ),
    val ratioBelowThreshold: Float = 0.01f,
    val outputDirectory: File,
    val gson: Gson = SocketReaderWriter.SPACE_ENG_GSON,
    val blockPosition: Vec3F = Vec3F(0, 1000, 0)
) {
    fun run() {
        se.definitions.blockDefinitions()
            .filterForScreenshots()
            .forEach {
                try {
                    saveMeta(takeScreenshots(it))
                } catch (e: Exception) {
                    println("error for $it")
                    e.printStackTrace()
                }
            }
    }

    fun saveMeta(screenshots: Screenshots) {
        File(blockOutputDirectory(screenshots.blockType), "${screenshots.blockType}.json").writeText(
            gson.toJson(
                screenshots
            )
        )

    }

    private fun List<BlockDefinition>.filterForScreenshots(): List<BlockDefinition> {
        return filter { it.cubeSize == CubeSize.Large && it.definitionId.type.isNotBlank() && it.enabled && it.public && it.mountPoints.isNotEmpty() }
    }

    private fun screenshotDistance(blockDefinition: BlockDefinition, orientationForward: Vec3F): Float {
        return if (blockDefinition.size == Vec3F.ONE) {
            5f
        } else {
            10f
        }
    }

    private fun blockOutputDirectory(blockType: String): File {
        return File(outputDirectory, blockType).apply { mkdirs() }
    }

    private fun fileName(
        blockDefinition: BlockDefinition,
        integrity: Float,
        namedOrientation: NamedOrientations,
        buildProgressModel: BuildProgressModel
    ): String {
        return "${blockDefinition.definitionId.type}_${namedOrientation.name}_${integrity}.png"
    }

    private fun takeScreenshots(blockDefinition: BlockDefinition): Screenshots {
        se.admin.character.teleport(position = blockPosition + Vec3F(x = 10))
        return Screenshots(
            blockType = blockDefinition.definitionId.type,
            screenshots = orientations.flatMap { namedOrientation ->
                takeScreenshotsOfSingleOrientation(blockDefinition, namedOrientation).apply {
                    sleep(50)
                }
            },
        )
    }

    private fun takeScreenshotsOfSingleOrientation(
        blockDefinition: BlockDefinition,
        namedOrientation: NamedOrientations
    ): List<SingleScreenshot> {
        se.observer.observeBlocks().allBlocks.forEach { se.admin.blocks.remove(it.id) }
        se.admin.blocks.placeAt(
            blockDefinition.definitionId,
            blockPosition,
            namedOrientation.orientationForward,
            namedOrientation.orientationUp
        )
        val block = se.observer.observeNewBlocks().allBlocks.last()
        se.admin.character.teleport(
            block.centerPosition + Vec3F(
                y = -DISTANCE_CENTER_CAMERA,
                z = screenshotDistance(
                    blockDefinition,
                    namedOrientation.orientationForward
                )
            ),
            Vec3F.FORWARD, Vec3F.UP
        )
        return (listOf(BuildProgressModel(1f)) + blockDefinition.buildProgressModels.reversed()).map { seBuildProgressModel ->
            val integrity = block.maxIntegrity * (seBuildProgressModel.buildRatioUpperBound - ratioBelowThreshold)
            se.admin.blocks.setIntegrity(block.id, integrity)
            sleep(100)
            val singleScreenshot = SingleScreenshot(
                orientationName = namedOrientation.name,
                integrity = integrity,
                buildRatioUpperBound = seBuildProgressModel.buildRatioUpperBound,
                filename = fileName(
                    blockDefinition = blockDefinition,
                    integrity = integrity,
                    namedOrientation = namedOrientation,
                    buildProgressModel = seBuildProgressModel
                )

            )
            se.observer.takeScreenshot(
                File(
                    blockOutputDirectory(blockDefinition.definitionId.type),
                    singleScreenshot.filename
                ).absolutePath
            )
            sleep(200)
            singleScreenshot
        }.apply {
            se.admin.blocks.remove(block.id)
            sleep(50)
        }
    }
}
