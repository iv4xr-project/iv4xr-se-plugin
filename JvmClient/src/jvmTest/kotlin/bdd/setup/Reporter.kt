package bdd.setup

import io.cucumber.java.Scenario
import io.cucumber.plugin.event.TestCaseFinished
import kotlinx.coroutines.*
import spaceEngineers.controller.connection.Config
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ProcessWithConnection
import spaceEngineers.controller.toFile
import java.io.File
import java.lang.Thread.sleep
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.absolutePathString


class Reporter(
    val connectionManager: ConnectionManager,
    val start: Instant = Instant.now(),
    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm"),
) {

    val config: Config = connectionManager.config

    val dir = File(config.outputDirectory, config.connectionSetupName + "/" + sdf.format(Date.from(start)))

    val screenshots = File(dir, "screenshots")

    val reports = File(dir, "reports")

    fun createDirectory() {
        dir.mkdirs()
        screenshots.mkdirs()
        reports.mkdirs()
        File(screenshots, "FAILED").mkdirs()
        File(screenshots, "PASSED").mkdirs()
    }

    fun saveReports() {
        copyFolder(
            "./build/cucumber-reports/".toFile().toPath(),
            reports.toPath(),
        )
    }

    suspend fun processScreenshots(scenario: Scenario) {
        connectionManager.processScreenshots(scenario)
    }

    suspend fun processScreenshots(scenario: TestCaseFinished) {
        connectionManager.processScreenshots(scenario)
    }

    suspend fun ConnectionManager.processScreenshots(scenario: Scenario) {
        if (!shouldTakeScreenshot(scenario, config.screenshotMode)) {
            return
        }
        games {
            processScreenshot(scenario)
        }
    }

    suspend fun ConnectionManager.processScreenshots(scenario: TestCaseFinished) {
        if (!shouldTakeScreenshot(scenario, config.screenshotMode)) {
            return
        }
        games {
            processScreenshot(scenario)
        }
    }

    fun ProcessWithConnection.processScreenshot(scenario: Scenario) {
        val abs = File(
            screenshots,
            "${scenario.status}/${
                scenario.uri.toString().substringAfterLast('/').substringBefore(".feature")
            }-${scenario.line}.png"
        ).absolutePath
        observer.takeScreenshot(abs)
    }

    fun ProcessWithConnection.processScreenshot(scenario: TestCaseFinished) {
        val abs = File(
            screenshots,
            "${scenario.result.status}/${
                scenario.testCase.uri.toString().substringAfterLast('/').substringBefore(".feature")
            }-${scenario.testCase.location.line}.png"
        ).absolutePath
        observer.takeScreenshot(abs)
    }

    companion object {

        fun copyFolder(source: Path, target: Path, vararg options: CopyOption) {
            Files.walkFileTree(source, object : SimpleFileVisitor<Path>() {

                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    Files.createDirectories(target.resolve(source.relativize(dir)))
                    return FileVisitResult.CONTINUE
                }


                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    Files.copy(file, target.resolve(source.relativize(file)), *options)
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }
}
