package spaceEngineers.iv4xr

import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent
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
    fun test_navigate_medical_room_and_restore_health() {
        val agentId = SpaceEngineers.DEFAULT_AGENT_ID
        val context = SpaceEngineersTestContext()

        // Create a controller instance of the SpaceEngineers interface.
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = JvmSpaceEngineersBuilder.default().localhost(agentId),
                context = context
            )

        // Create iv4xr environment and pass ID of the world (scenario to load).
        val seEnv = SeEnvironment(
            controller = controllerWrapper,
            worldId = "LoneSurvivor_SpaceShip"
        )

        // Create the iv4XR test agent
        val seAgentState = SeAgentState(agentId = agentId)
        val testAgent = TestAgent(agentId, "long play goal solving agent")
            .attachState(seAgentState)
            .attachEnvironment(seEnv)

        val navigateAimToBlock = GoalBuilder().navigateAimToBlock(
            "MedicalRoom",
            5f, // Maximum distance allowed
            tactic = SEQ(
                TacticLib().navigateToBlock(
                    desiredBlock = "MedicalRoom",
                    closestDistance = 5f,
                    distancePathTolerance = 3f
                ),
                TacticLib().rotateToBlock("MedicalRoom")
            )
        )

        val reduceHealthBelow = GoalBuilder().agentHealthIsBelow(
            90.00, // Maximum health allowed
            tactic = SEQ(
                TacticLib().setHelmet(false, 5000),
                TacticLib().setHelmet(true)
            )
        )

        val restoreHealthAbove = GoalBuilder().agentHealthIsAbove(
            99.99, // Minimum health allowed
            tactic = TacticLib().continuousUse(5000)
        )

        val goalStructure: GoalStructure = SEQ(
            navigateAimToBlock,
            reduceHealthBelow,
            restoreHealthAbove
        )

        testAgent.setGoal(goalStructure)

        // Load the scenario.
        seEnv.loadWorld()
        seEnv.controller.screens.waitUntilTheGameLoaded()
        Thread.sleep(500)

        // Run the test agent to accomplish the attached Goal
        var i = 0
        while (goalStructure.status.inProgress() && i <= 10) {
            testAgent.update()
            println("*** $i, ${seAgentState.worldmodel.agentId} @${seAgentState.worldmodel.position}")
            i++
        }

        // Print the test agent results
        goalStructure.printGoalStructureStatus()
        assertTrue { goalStructure.status.success() }

        // Exit the scenario
        seEnv.exitLevelWithoutSaving()
        seEnv.close()
    }
}
