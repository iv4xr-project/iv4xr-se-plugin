package spaceEngineers.iv4xr

import environments.SeEnvironment
import environments.observe
import eu.iv4xr.framework.mainConcepts.TestAgent
import eu.iv4xr.framework.mainConcepts.TestDataCollector
import eu.iv4xr.framework.mainConcepts.W3DAgentState
import eu.iv4xr.framework.spatial.Vec3
import nl.uu.cs.aplib.AplibEDSL.SEQ
import nl.uu.cs.aplib.mainConcepts.GoalStructure
import org.junit.jupiter.api.Test
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.OldProtocolSpaceEngineers
import spaceEngineers.controller.ProprietaryJsonTcpCharacterController
import spaceEngineers.controller.SpaceEngineersTestContext
import spaceEngineers.iv4xr.goal.GoalBuilder
import spaceEngineers.iv4xr.goal.TacticLib
import spaceEngineers.model.ToolbarLocation
import kotlin.test.assertTrue


class BasicIv4xrTest {
    @Test
    fun placeGrindDownTorchUp() {
        val agentId = "agentId"
        val blockType = "LargeHeavyBlockArmorBlock"
        val context = SpaceEngineersTestContext()
        context.blockTypeToToolbarLocation[blockType] = ToolbarLocation(1, 0)
        val controller = ProprietaryJsonTcpCharacterController.localhost(agentId)
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = OldProtocolSpaceEngineers(controller),
                context = context
            )
        val theEnv = SeEnvironment(
            controller = controllerWrapper,
            worldId = "simple-place-grind-torch-with-tools",
            context = context,
            worldController = controller
        )
        theEnv.loadWorld()
        theEnv.observeForNewBlocks()

        val dataCollector = TestDataCollector()

        val myAgentState = W3DAgentState()


        val testAgent = TestAgent(agentId, "some role name, else nothing")
            .attachState(myAgentState)
            .attachEnvironment(theEnv)
            .setTestDataCollector(dataCollector)


        val goals = GoalBuilder()
        val tactics = TacticLib()
        val testingTask: GoalStructure = SEQ(
            goals.agentAtPosition(Vec3(532.7066f, -45.193184f, -24.395466f), epsilon = 0.001f),
            goals.agentDistanceFromPosition(
                Vec3(532.7066f, -45.193184f, -23.946253f),
                distance = 16f,
                epsilon = 0.1f,
                tactic = tactics.moveForward(1f),
            ),
            goals.blockOfTypeExists(
                blockType,
                tactic = tactics.buildBlock(blockType),
            ),
            goals.lastBuiltBlockIntegrityIsBelow(
                percentage = 0.1,
                tactic = SEQ(
                    tactics.equip(ToolbarLocation(5, 0)),
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
                    tactics.equip(ToolbarLocation(4, 0)),
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

        var i = 0
        myAgentState.observe()
        while (testingTask.status.inProgress() && i <= 1500) {
            println("*** $i, ${myAgentState.wom.agentId} @${myAgentState.wom.position}")
            i++
            testAgent.update()
        }

        testingTask.printGoalStructureStatus()
        testingTask.subgoals.forEach { assertTrue(it.status.success()) }
    }
}