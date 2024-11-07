package bdd.iv4XR.steps

import bdd.iv4XR.state.SeScenarioState
import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent
import eu.iv4xr.framework.mainConcepts.TestDataCollector
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import nl.uu.cs.aplib.AplibEDSL.SEQ
import nl.uu.cs.aplib.mainConcepts.GoalStructure
import nl.uu.cs.aplib.mainConcepts.ProgressStatus
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JvmSpaceEngineersBuilder
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.SpaceEngineersTestContext
import spaceEngineers.iv4xr.goal.GoalBuilder
import spaceEngineers.iv4xr.goal.TacticLib
import java.io.File
import java.nio.file.Paths
import kotlin.test.assertTrue

class SeScenarioStepsDefinition {

    private val seScenarioState: SeScenarioState = SeScenarioState()

    @Given("the agent loads the world {string}.")
    fun agentLoadsScenario(worldId: String) {
        seScenarioState.agentId = SpaceEngineers.DEFAULT_AGENT_ID
        val context = SpaceEngineersTestContext()

        // Create a controller instance of the SpaceEngineers interface.
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = JvmSpaceEngineersBuilder.default().localhost(seScenarioState.agentId!!),
                context = context
            )

        // Prepare the path to the game-saves directory.
        val scenarioDir = Paths.get(System.getProperty("user.dir"))
            .parent
            .resolve("JvmClient/src/jvmTest/resources/game-saves/")
            .toAbsolutePath()
            .toString()

        // Create iv4xr environment and pass ID of the world (scenario to load).
        seScenarioState.seEnvironment = SeEnvironment(
            controller = controllerWrapper,
            worldId = worldId,
            scenarioDir = "$scenarioDir${File.separator}"
        )

        // Load the scenario.
        seScenarioState.seEnvironment!!.loadWorld()
        Thread.sleep(1000)

        // Create the iv4XR test agent
        seScenarioState.seAgentState = SeAgentState(agentId = seScenarioState.agentId!!)
        val dataCollector = TestDataCollector()
        seScenarioState.testAgent = TestAgent(seScenarioState.agentId, "goal solving agent")
            .attachState(seScenarioState.seAgentState)
            .attachEnvironment(seScenarioState.seEnvironment)
            .setTestDataCollector(dataCollector)
    }

    @Given("the agent observes the block type {string}.")
    fun agentObservesBlock(blockType: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().blockOfTypeExists(
                blockType,
                tactic = TacticLib().observe(),
            )
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent navigates to the 2x2 block type {string} with a maximum {float}.")
    fun agentNavigatesTo2x2Block(blockType: String, distance: Float) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().navigateNearToBlock(
                blockType,
                distance,
                tactic = TacticLib().groundedNavigationNearToBlock(blockType, distance) { it.maxPosition },
            )
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent aims the block type {string}.")
    fun agentAimsTheBlock(blockType: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().aimToBlock(
                blockType,
                tactic = TacticLib().rotateToBlock(blockType),
            )
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent removes the helmet {long} milliseconds.")
    fun agentRemovesHelmet(milliseconds: Long) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().agentHealthIsBelow(
                100.00,
                tactic = TacticLib().setHelmet(false, milliseconds)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent activates the helmet.")
    fun agentActivatesHelmet() {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().setHelmet(true)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent continuously uses the terminal {long} milliseconds.")
    fun agentContinuouslyUses(milliseconds: Long) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().continuousUse(milliseconds)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @Then("the agent health is below {double} percentage.")
    fun agentHealthBelow(percentage: Double) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().agentHealthIsBelow(
                percentage,
                tactic = TacticLib().doNothing()
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @Then("the agent health is above {double} percentage.")
    fun agentHealthAbove(percentage: Double) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().agentHealthIsAbove(
                percentage,
                tactic = TacticLib().doNothing()
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    private fun executeGoal(goalStructure: GoalStructure):ProgressStatus{
        seScenarioState.testAgent?.setGoal(goalStructure)
        var i = 0
        while (goalStructure.status.inProgress() && i <= 5) {
            seScenarioState.testAgent?.update()
            println("*** $i, ${seScenarioState.seAgentState?.worldmodel?.agentId} @${seScenarioState.seAgentState?.worldmodel?.position}")
            i++
        }

        goalStructure.printGoalStructureStatus()
        return goalStructure.status
    }

    @After
    fun closeWorldScenario() {
        seScenarioState.seEnvironment?.exitLevelWithoutSaving()
        seScenarioState.seEnvironment?.close()
    }
}