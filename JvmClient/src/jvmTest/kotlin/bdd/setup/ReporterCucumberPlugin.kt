package bdd.setup

import bdd.globalCm
import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.*
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep


class ReporterCucumberPlugin @JvmOverloads constructor(
    val outputDirectory: String = "./reports/"
) : ConcurrentEventListener {

    val reporter by lazy { Reporter(connectionManager = globalCm) }

    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(TestRunStarted::class.java, ::onStarted)
        publisher.registerHandlerFor(TestRunFinished::class.java, ::onFinished)
        publisher.registerHandlerFor(TestCaseStarted::class.java, ::onScenarioStarted)
        publisher.registerHandlerFor(TestCaseFinished::class.java, ::onScenarioFinished)
    }

    fun onStarted(event: TestRunStarted) {
        reporter.createDirectory()
    }

    fun onFinished(event: TestRunFinished) {
        sleep(10000)
        reporter.saveReports()
    }

    fun onScenarioStarted(event: TestCaseStarted) {
    }

    fun onScenarioFinished(event: TestCaseFinished) = runBlocking {
        reporter.processScreenshots(event)
    }

}
