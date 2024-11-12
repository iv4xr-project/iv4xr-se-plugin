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
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.ToolbarLocation
import kotlin.test.assertTrue

class GoalPlatformTest {

    @Disabled
    @Test
    fun test_container_interaction() {
        val agentId = DEFAULT_AGENT_ID
        val context = SpaceEngineersTestContext()

        val welder = DefinitionId.physicalGun("Welder2Item") // We will use this welder.
        val welderLocation = ToolbarLocation(2, 0) // We will put welder here in the toolbar.
        val grinder = DefinitionId.physicalGun("AngleGrinder2Item") // We will use this grinder.
        val grinderLocation = ToolbarLocation(3, 0) // We will put grinder here in the toolbar.

        // Create a controller instance of the SpaceEngineers interface.
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = JvmSpaceEngineersBuilder.default().localhost(agentId),
                context = context
            )

        // Create iv4xr environment and pass ID of the world (scenario to load).
        val theEnv = SeEnvironment(
            controller = controllerWrapper,
            worldId = "SimplePlatform"
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

        val cryoChamber = Pair("CryoChamber", 3.5f)
        val gravityBlock = Pair("Gravity", 3.5f)
        val survivalKit = Pair("Survival", 3.5f)

        val goalStructure: GoalStructure = AplibEDSL.SEQ(
            // Navigate Interact with CryoChamber
            goals.navigateNearToBlock(
                cryoChamber.first,
                cryoChamber.second,
                tactic = tactics.navigateToBlock(cryoChamber.first, cryoChamber.second),
            ),
            goals.aimToBlock(
                cryoChamber.first,
                tactic = tactics.rotateToBlock(cryoChamber.first)
            ),
            goals.alwaysSolved(
                tactic = tactics.use()
            ),
            goals.alwaysSolved(
                tactic = tactics.sleep(1000)
            ),
            goals.alwaysSolved(
                tactic = tactics.use()
            ),
            // Navigate Grinder and Welder Gravity block
            goals.navigateNearToBlock(
                gravityBlock.first,
                gravityBlock.second,
                tactic = AplibEDSL.SEQ(
                    tactics.equip(grinderLocation),
                    tactics.sleep(500),
                    tactics.navigateToBlock(gravityBlock.first, gravityBlock.second)
                )
            ),
            goals.aimToBlock(
                gravityBlock.first,
                tactic = tactics.rotateToBlock(gravityBlock.first)
            ),
            goals.typeBuiltBlockIntegrityIsBelow(
                gravityBlock.first,
                percentage = 0.5,
                tactic = tactics.startUsingTool()
            ),
            goals.alwaysSolved(
                tactic = tactics.endUsingTool()
            ),
            goals.typeBuiltBlockIntegrityIsAbove(
                gravityBlock.first,
                percentage = 1.0,
                tactic = AplibEDSL.SEQ(
                    tactics.equip(welderLocation),
                    tactics.sleep(500),
                    tactics.startUsingTool()
                ),
            ),
            goals.alwaysSolved(
                tactic = tactics.endUsingTool()
            ),
            // Navigate to Survival Kit
            goals.navigateNearToBlock(
                survivalKit.first,
                survivalKit.second,
                tactic = tactics.navigateToBlock(survivalKit.first, survivalKit.second),
            ),
            goals.aimToBlock(
                survivalKit.first,
                tactic = AplibEDSL.SEQ(
                    tactics.rotateToBlock(survivalKit.first),
                    tactics.sleep(500),
                    tactics.equip(grinderLocation)
                )
            ),
            goals.terminalIsOpened(
                tactic = tactics.use()
            ),
            goals.alwaysSolved(
                tactic = tactics.sleep(1000)
            ),
            goals.alwaysSolved(
                tactic = tactics.closeTerminal()
            ),
            goals.alwaysSolved(
                tactic = tactics.sleep(1000)
            )
        )
        testAgent.setGoal(goalStructure)

        // Load the scenario.
        theEnv.loadWorld()
        theEnv.controller.screens.waitUntilTheGameLoaded()
        // Setup welder in the toolbar.
        theEnv.controller.items.setToolbarItem(welder, welderLocation)
        // Setup grinder in the toolbar.
        theEnv.controller.items.setToolbarItem(grinder, grinderLocation)
        Thread.sleep(500)

        // Run the test agent to accomplish the attached Goal
        var i = 0
        while (goalStructure.status.inProgress() && i <= 200) {
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
