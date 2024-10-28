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

class TacticGroundedNavigationTest {

    @Disabled
    @Test
    fun test_goal_navigate_CryoChamber() {
        val agentId = DEFAULT_AGENT_ID
        val context = SpaceEngineersTestContext()

        // Create a controller instance of the SpaceEngineers interface.
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = JvmSpaceEngineersBuilder.default().localhost(agentId),
                context = context
            )

        // Create iv4xr environment and pass ID of the world (scenario to load).
        val theEnv = SeEnvironment(
            controller = controllerWrapper,
            worldId = "small",
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
        val cryoChamber = Pair("BlockCryoChamber", 3f)
        val testingTask: GoalStructure = AplibEDSL.SEQ(
            goals.navigateNearToBlock(
                cryoChamber.first,
                cryoChamber.second,
                tactic = tactics.groundedNavigationNearToBlock(cryoChamber.first, cryoChamber.second),
            )
        )
        testAgent.setGoal(testingTask)

        // Load the scenario.
        theEnv.loadWorld()
        theEnv.controller.screens.waitUntilTheGameLoaded()

        // Run the test agent to accomplish the attached Goal
        // In a ground navigation tactic, the agent navigates the whole path in one step
        var i = 0
        while (testingTask.status.inProgress() && i <= 10) {
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
