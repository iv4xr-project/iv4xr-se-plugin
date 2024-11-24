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
import spaceEngineers.controller.SpaceEngineersTestContext
import spaceEngineers.iv4xr.goal.GoalBuilder
import spaceEngineers.iv4xr.goal.TacticLib
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import java.io.File
import java.nio.file.Paths
import kotlin.test.assertTrue

class SeScenarioStepsDefinition {

    private val seScenarioState: SeScenarioState = SeScenarioState()

    @Given("the agent-id is {string}")
    fun agentId(agentId: String) {
        seScenarioState.agentId = agentId
    }

    @Given("the agent loads the world {string}")
    fun agentLoadsScenario(worldId: String) {
        val context = SpaceEngineersTestContext()

        // Create a controller instance of the SpaceEngineers interface.
        val controllerWrapper =
            ContextControllerWrapper(
                spaceEngineers = JvmSpaceEngineersBuilder.default().localhost(seScenarioState.agentId),
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
        seScenarioState.seAgentState = SeAgentState(agentId = seScenarioState.agentId)
        val dataCollector = TestDataCollector()
        seScenarioState.testAgent = TestAgent(seScenarioState.agentId, "goal solving agent")
            .attachState(seScenarioState.seAgentState)
            .attachEnvironment(seScenarioState.seEnvironment)
            .setTestDataCollector(dataCollector)
    }

    @Given("the agent observes the block {string}")
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

    @When("the agent navigates to the block {string}")
    fun agentNavigatesToBlock(blockType: String) {
        val closestDistance = if (is1x1Block(blockType)) 3.2f else 5f
        val distancePathTolerance = if (is1x1Block(blockType)) 1.2f else 3f

        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().navigateNearToBlock(
                blockType,
                closestDistance,
                tactic = TacticLib().navigateToBlock(
                    desiredBlock = blockType,
                    closestDistance = closestDistance,
                    movementType = CharacterMovementType.RUN,
                    distancePathTolerance = distancePathTolerance
                )
            )
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent navigates to the assembler block {string}")
    fun agentNavigatesToAssemblerBlock(blockType: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().navigateNearToBlock(
                blockType,
                tactic = TacticLib().navigateToBlock(
                    desiredBlock = blockType,
                    movementType = CharacterMovementType.RUN
                )
            )
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent aims the block {string}")
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

    @When("the agent removes the helmet {long} milliseconds")
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

    @When("the agent activates the helmet")
    fun agentActivatesHelmet() {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().setHelmet(true)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent uses the terminal {long} milliseconds")
    fun agentContinuouslyUsesTerminal(milliseconds: Long) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().continuousUse(milliseconds)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent equips the tool {string}")
    fun agentEquipsTool(tool: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().equip(tool)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent uses the tool {long} milliseconds")
    fun agentContinuouslyUsesTool(milliseconds: Long) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().useToolTime(milliseconds)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent drops the item {string} from the inventory")
    fun agentDropsItemFromInventory(item: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().dropItem(item)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent drops all the items from the inventory")
    fun agentDropsItemFromInventory() {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().cleanInventoryItems()
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent rotates {long} milliseconds")
    fun agentRotates(milliseconds: Long) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().rotateMilliseconds(milliseconds)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @When("the agent builds the assembler block {string}")
    fun agentBuildsAssemblerBlock(blockType: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().alwaysSolved(
                tactic = TacticLib().buildAssemblerBlock(blockType)
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @Then("the maximum distance to the block {string} is {float}")
    fun agentMaximumDistanceToBlock(blockType: String, goalDistance: Float) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().navigateNearToBlock(
                blockType,
                goalDistance,
                tactic = TacticLib().doNothing()
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @Then("the agent health is below {double}")
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

    @Then("the agent health is above {double}")
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

    @Then("the agent inventory contains more than {int} units of the item {string}")
    fun agentInventoryContainsUnitsItem(units: Int, item: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().inventoryContainsItem(
                item,
                units,
                tactic = TacticLib().doNothing()
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @Then("the agent inventory does not contain the item {string}")
    fun agentInventoryDoesNotContain(item: String) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().inventoryDoesNotContainItem(
                item,
                tactic = TacticLib().doNothing()
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    @Then("the integrity of block {string} is above {double} percentage")
    fun blockIntegrityIsAbove(blockType: String, integrity: Double) {
        val goalStructure: GoalStructure = SEQ(
            GoalBuilder().typeBuiltBlockIntegrityIsAbove(
                blockType,
                integrity / 100,
                tactic = TacticLib().doNothing()
            ),
        )

        val status = executeGoal(goalStructure)
        assertTrue(status.success())
    }

    private fun is1x1Block(blockType: String):Boolean{
        for (block in seScenarioState.seEnvironment?.controller?.observer?.observeBlocks()?.allBlocks!!) {
            if (blockType in block.definitionId.toString()) {
                return block.size == Vec3F(1,1,1)
            }
        }
        return false
    }

    private fun executeGoal(goalStructure: GoalStructure):ProgressStatus{
        seScenarioState.testAgent?.setGoal(goalStructure)
        var i = 0
        while (goalStructure.status.inProgress() && i <= 20) {
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