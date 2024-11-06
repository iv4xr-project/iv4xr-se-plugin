package spaceEngineers.iv4xr

import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent
import eu.iv4xr.framework.mainConcepts.TestDataCollector
import nl.uu.cs.aplib.AplibEDSL.SEQ
import nl.uu.cs.aplib.mainConcepts.GoalStructure
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JvmSpaceEngineersBuilder
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.SpaceEngineersTestContext
import spaceEngineers.iv4xr.goal.GoalBuilder
import spaceEngineers.iv4xr.goal.TacticLib
import kotlin.test.assertTrue

class LoneSurvivorMedicalRoomTest {

    @Disabled
    @Test
    fun test_health_navigation_to_medical() {
        val agentId = SpaceEngineers.DEFAULT_AGENT_ID
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
            worldId = "LoneSurvivor_SpaceShip"
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

        val medical = Pair("Medical", 7f)

        val goalStructure: GoalStructure = SEQ(
            // Navigate Interact with the medical room
            goals.navigateNearToBlock(
                medical.first,
                medical.second,
                tactic = tactics.groundedNavigationNearToBlock(medical.first, medical.second) { it.maxPosition },
            ),
            goals.aimToBlock(
                medical.first,
                tactic = tactics.rotateToBlock(medical.first)
            ),
            goals.agentHealthIsBelow(
                90.00,
                tactic = tactics.setHelmet(false, 5000)
            ),
            goals.agentHealthIsAbove(
                99.99,
                tactic = SEQ(
                    tactics.setHelmet(true),
                    tactics.continuousUse(5000, 1000)
                )
            )
        )
        testAgent.setGoal(goalStructure)

        // Load the scenario.
        theEnv.loadWorld()
        theEnv.controller.screens.waitUntilTheGameLoaded()
        Thread.sleep(500)

        // Run the test agent to accomplish the attached Goal
        var i = 0
        while (goalStructure.status.inProgress() && i <= 10) {
            testAgent.update()
            println("*** $i, ${myAgentState.worldmodel.agentId} @${myAgentState.worldmodel.position}")
            i++
        }

        // Print the test agent results
        goalStructure.printGoalStructureStatus()
        assertTrue { goalStructure.status.success() }

        // Exit the scenario
        theEnv.exitLevelWithoutSaving()
        theEnv.close()
    }
}
