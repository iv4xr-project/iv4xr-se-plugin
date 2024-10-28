package spaceEngineers.iv4xr

import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent
import eu.iv4xr.framework.mainConcepts.TestDataCollector
import nl.uu.cs.aplib.AplibEDSL
import nl.uu.cs.aplib.mainConcepts.GoalStructure
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JvmSpaceEngineersBuilder
import spaceEngineers.controller.SpaceEngineers.Companion.DEFAULT_AGENT_ID
import spaceEngineers.controller.SpaceEngineersTestContext
import spaceEngineers.iv4xr.goal.GoalBuilder
import spaceEngineers.iv4xr.goal.TacticLib
import kotlin.test.assertTrue

class TacticDynamicNavigationTest {

    @Disabled
    @Test
    fun test_goal_navigate_ButtonPanel() {
        val agentId = DEFAULT_AGENT_ID
        val context = SpaceEngineersTestContext()

        // Create a controller instance of the SpaceEngineers interface.
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = JvmSpaceEngineersBuilder.default().localhost(agentId),
                context = context
            )

        // Create iv4xr environment and pass ID of the world (scenario to load).
        // INFO: You will need to create the Learning to Survive scenario before running this test
        val theEnv = SeEnvironment(
            controller = controllerWrapper,
            worldId = "Learning to Survive\\Mission01",
            scenarioDir = "C:\\Users\\username\\AppData\\Roaming\\SpaceEngineers\\Saves\\765611XYZNMT\\"
        )

        // Create the iv4XR test agent
        val myAgentState = SeAgentState(agentId = agentId)
        val dataCollector = TestDataCollector()
        val testAgent = TestAgent(agentId, "goal solving agent")
            .attachState(myAgentState)
            .attachEnvironment(theEnv)
            .setTestDataCollector(dataCollector)

        // Create the desired testing goals and tactics.
        val goals = GoalBuilder()
        val tactics = TacticLib()
        val buttonPanel = Pair("ButtonPanelLarge", 4f)
        val testingTask: GoalStructure = AplibEDSL.SEQ(
            goals.navigateNearToBlock(
                buttonPanel.first,
                buttonPanel.second,
                tactic = tactics.dynamicNavigationNearToBlock(buttonPanel.first, buttonPanel.second),
            )
        )
        testAgent.setGoal(testingTask)

        // Load the scenario.
        theEnv.loadWorld()
        theEnv.controller.screens.waitUntilTheGameLoaded()

        // Run the test agent to accomplish the attached Goal
        // In a dynamic navigation tactic, the agent re-calculates the navigation step-by-step
        var i = 0
        while (testingTask.status.inProgress() && i <= 20) {
            testAgent.update()
            println("*** $i, ${myAgentState.worldmodel.agentId} @${myAgentState.worldmodel.position}")
            i++
        }

        // Print the test agent results
        testingTask.printGoalStructureStatus()
        testingTask.subgoals.forEach { assertTrue(it.status.success()) }

        // Exit the scenario
        theEnv.exitLevelWithoutSaving()
        theEnv.close()
    }
}
