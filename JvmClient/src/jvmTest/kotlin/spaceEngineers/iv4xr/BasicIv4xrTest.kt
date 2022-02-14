package spaceEngineers.iv4xr

import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent
import eu.iv4xr.framework.mainConcepts.TestDataCollector
import eu.iv4xr.framework.spatial.Vec3
import nl.uu.cs.aplib.AplibEDSL.SEQ
import nl.uu.cs.aplib.mainConcepts.GoalStructure
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import spaceEngineers.controller.*
import spaceEngineers.controller.SpaceEngineers.Companion.DEFAULT_AGENT_ID
import spaceEngineers.iv4xr.goal.GoalBuilder
import spaceEngineers.iv4xr.goal.TacticLib
import spaceEngineers.model.ToolbarLocation
import kotlin.test.assertTrue


class BasicIv4xrTest {

    @Disabled("Disabled for building whole project, enable manually by uncommenting.")
    @Test
    fun placeGrindDownTorchUp() {
        // Setup constants to use later.
        val agentId = DEFAULT_AGENT_ID //agent id
        val blockType = "LargeHeavyBlockArmorBlock" // Block type that we will operate with (it's a cube block).
        val context = SpaceEngineersTestContext() // This context saves recent information about operations (for example last built blocks and all the observations).
        val blockLocation = ToolbarLocation(1, 0) // We will put block here in the toolbar.
        val welder = "Welder2Item" // We will use this welder.
        val welderLocation = ToolbarLocation(2, 0) // We will put welder here in the toolbar.
        val grinder = "AngleGrinder2Item" // We will use this grinder.
        val grinderLocation = ToolbarLocation(3, 0) // We will put grinder here in the toolbar.
        // We map position of the block in the toolbar.
        context.blockTypeToToolbarLocation[blockType] = blockLocation
        // We create instance of SpaceEngineers interface. ContextControllerWrapper is "smarter" implementation, that saves recent information into context created above.
        // Otherwise, JsonRpcSpaceEngineersBuilder.localhost(agentId) can be used directly (also SpaceEngineers interface implementation).
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = JVMSpaceEngineersBuilder.default().localhost(agentId),
                context = context
            )
        // We create iv4xr environment and pass ID of the world (scenario to load).
        val theEnv = SeEnvironment(
            controller = controllerWrapper,
            worldId = "simple-place-grind-torch-with-tools",
        )


        // Creating IV4XR related classes.
        val dataCollector = TestDataCollector()

        val myAgentState = SeAgentState(agentId = agentId)

        // Assemble agent.
        val testAgent = TestAgent(agentId, "some role name, else nothing")
            .attachState(myAgentState)
            .attachEnvironment(theEnv)
            .setTestDataCollector(dataCollector)


        val goals = GoalBuilder()
        val tactics = TacticLib()
        // Create goals and tactics.
        val testingTask: GoalStructure = SEQ(
            goals.agentAtPosition(Vec3(532.7066f, -45.193184f, -24.395466f), epsilon = 0.05f),
            goals.agentDistanceFromPosition(
                Vec3(532.7066f, -45.193184f, -23.946253f),
                distance = 16f,
                epsilon = 0.1f,
                tactic = tactics.moveForward(),
            ),
            goals.blockOfTypeExists(
                blockType,
                tactic = tactics.buildBlock(blockType),
            ),
            goals.lastBuiltBlockIntegrityIsBelow(
                percentage = 0.1,
                tactic = SEQ(
                    tactics.equip(grinderLocation),
                    tactics.sleep(500),
                    tactics.startUsingTool(),
                ),
            ),
            goals.alwaysSolved(
                tactic = SEQ(
                    tactics.endUsingTool(),
                    tactics.sleep(500),
                ),
            ),
            goals.lastBuiltBlockIntegrityIsAbove(
                percentage = 1.0,
                tactic = SEQ(
                    tactics.equip(welderLocation),
                    tactics.sleep(500),
                    tactics.startUsingTool(),
                ),
            ),
            goals.alwaysSolved(
                tactic = SEQ(
                    tactics.endUsingTool(),
                ),
            ),
        )

        testAgent.setGoal(testingTask)

        // We load the scenario.
        theEnv.loadWorld()
        // Setup block in the toolbar.
        controllerWrapper.items.setToolbarItem(blockType, blockLocation)
        // Setup welder in the toolbar.
        controllerWrapper.items.setToolbarItem(welder, welderLocation)
        // Setup grinder in the toolbar.
        controllerWrapper.items.setToolbarItem(grinder, grinderLocation)
        Thread.sleep(500)

        // We observe for new blocks once, so that current blocks are not going to be considered "new".
        theEnv.observeForNewBlocks()

        // Run the agent and update in the loop.
        var i = 0
        while (testingTask.status.inProgress() && i <= 1500) {
            testAgent.update()
            println("*** $i, ${myAgentState.wom.agentId} @${myAgentState.wom.position}")
            i++
        }

        // Print results.
        testingTask.printGoalStructureStatus()
        testingTask.subgoals.forEach { assertTrue(it.status.success()) }
    }
}
