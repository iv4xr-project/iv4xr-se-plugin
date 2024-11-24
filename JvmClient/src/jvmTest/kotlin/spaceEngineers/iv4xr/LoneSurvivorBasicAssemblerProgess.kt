package spaceEngineers.iv4xr

import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent
import nl.uu.cs.aplib.AplibEDSL
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

class LoneSurvivorBasicAssemblerProgress {

    @Disabled
    @Test
    fun test_basic_assembler_progress() {
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

        val navigateAimToHeavyBlock = GoalBuilder().navigateAimToBlock(
            "HeavyBlock",
            tactic = AplibEDSL.SEQ(
                // Navigate to Heavy blocks
                TacticLib().equip("AngleGrinderItem"),
                TacticLib().navigateToBlock("HeavyBlock"),
                TacticLib().rotateToBlock("HeavyBlock")
            )
        )

        val grinderToObtain40SteelPlates = GoalBuilder().inventoryContainsItem(
            "SteelPlate",
            40,
            tactic = AplibEDSL.SEQ(
                // Grinder to obtain 40 steel plates
                TacticLib().equip("AngleGrinderItem"),
                TacticLib().rotateToBlock("HeavyBlock"),
                TacticLib().useToolTime(5000)
            )
        )

        val dropMetalGridsFromInventory = GoalBuilder().inventoryDoesNotContainItem(
            "MetalGrid",
            tactic = AplibEDSL.SEQ(
                // Drop MetalGrid items that occupy inventory space
                TacticLib().dropItem("MetalGrid")
            )
        )

        val grinderToObtain60SteelPlates = GoalBuilder().inventoryContainsItem(
            "SteelPlate",
            60,
            tactic = AplibEDSL.SEQ(
                // Grinder to obtain 60 steel plates
                TacticLib().equip("AngleGrinderItem"),
                TacticLib().rotateToBlock("HeavyBlock"),
                TacticLib().useToolTime(5000)
            )
        )

        val createBasicAssemblerBlock = GoalBuilder().blockOfTypeExists(
            "BasicAssembler",
            tactic = AplibEDSL.SEQ(
                TacticLib().endUsingTool(),
                TacticLib().rotateMilliseconds(2000),
                // Create the BasicAssembler block skeleton
                TacticLib().buildAssemblerBlock("BasicAssembler")
            )
        )

        val navigateWelderBasicAssemblerTo60percent = GoalBuilder().typeBuiltBlockIntegrityIsAbove(
            "BasicAssembler",
            0.6, // Until 60% of integrity we need steel plates
            tactic = AplibEDSL.SEQ(
                // Navigate back to the BasicAssembler block to use the steel plates for welder
                TacticLib().equip("WelderItem"),
                TacticLib().navigateToBlock("BasicAssembler"),
                TacticLib().rotateToBlock("BasicAssembler"),
                TacticLib().useToolTime(15000)
            )
        )

        val navigateAimToLargeAssembler = GoalBuilder().navigateAimToBlock(
            "Assembler/LargeAssembler",
            tactic = AplibEDSL.SEQ(
                // Drop items that occupy inventory space
                TacticLib().cleanInventoryItems(),
                // Navigate to LargeAssembler block
                TacticLib().equip("AngleGrinderItem"),
                TacticLib().navigateToBlock("Assembler/LargeAssembler"),
                TacticLib().rotateToBlock("Assembler/LargeAssembler")
            )
        )

        val grinderToObtainConstructionComponents = GoalBuilder().inventoryContainsItem(
            "Construction",
            tactic = AplibEDSL.SEQ(
                // Navigate to LargeAssembler blocks to obtain construction components
                TacticLib().equip("AngleGrinderItem"),
                TacticLib().rotateToBlock("Assembler/LargeAssembler"),
                TacticLib().useToolTime(8000)
            )
        )

        val navigateWelderBasicAssemblerTo100percent = GoalBuilder().typeBuiltBlockIntegrityIsAbove(
            "BasicAssembler",
            0.99, // Until 100% of integrity we need construction components
            tactic = AplibEDSL.SEQ(
                // Navigate back to the BasicAssembler block to use the construction components for welder
                TacticLib().equip("WelderItem"),
                TacticLib().navigateToBlock("BasicAssembler"),
                TacticLib().rotateToBlock("BasicAssembler"),
                TacticLib().useToolTime(15000)
            )
        )

        val goalStructure: GoalStructure = AplibEDSL.SEQ(
            navigateAimToHeavyBlock,
            grinderToObtain40SteelPlates,
            dropMetalGridsFromInventory,
            grinderToObtain60SteelPlates,
            createBasicAssemblerBlock,
            navigateWelderBasicAssemblerTo60percent,
            navigateAimToLargeAssembler,
            grinderToObtainConstructionComponents,
            navigateWelderBasicAssemblerTo100percent
        )

        testAgent.setGoal(goalStructure)

        // Load the scenario.
        seEnv.loadWorld()
        seEnv.controller.screens.waitUntilTheGameLoaded()
        Thread.sleep(500)

        // Run the test agent to accomplish the attached Goal
        var i = 0
        while (goalStructure.status.inProgress() && i <= 50) {
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
