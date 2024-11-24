package spaceEngineers.iv4xr.goal

import environments.SeAgentState
import eu.iv4xr.framework.spatial.Vec3
import nl.uu.cs.aplib.AplibEDSL.SEQ
import nl.uu.cs.aplib.mainConcepts.Goal
import nl.uu.cs.aplib.mainConcepts.GoalStructure
import nl.uu.cs.aplib.mainConcepts.Tactic
import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.model.Block
import spaceEngineers.model.extensions.allBlocks
import kotlin.math.abs

class GoalBuilder(
    val tactics: TacticLib = TacticLib()
) {

    fun agentAtPosition(position: Vec3, epsilon: Float = 0.001f, tactic: Tactic = tactics.doNothing()): GoalStructure {
        return agentDistanceFromPosition(position = position, distance = 0f, epsilon = epsilon, tactic = tactic)
    }

    fun agentDistanceFromPosition(
        position: Vec3,
        distance: Float = 0f,
        epsilon: Float = 0.001f,
        tactic: Tactic
    ): GoalStructure.PrimitiveGoal {
        val goal =
            Goal("Agent at position $position with epsilon $epsilon")
                .toSolve { belief: SeAgentState ->
                    abs(Vec3.dist(belief.worldmodel.position, position) - distance) < epsilon
                }
                .withTactic(
                    tactic
                )
        return goal.lift()
    }

    fun agentHealthIsBelow(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("agentHealthIsBelow($percentage)")
            .toSolve { belief: SeAgentState ->
                return@toSolve belief.seEnv.controller.observer.observe().health * 100 < percentage
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun agentHealthIsAbove(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("agentHealthIsAbove($percentage)")
            .toSolve { belief: SeAgentState ->
                return@toSolve belief.seEnv.controller.observer.observe().health * 100 > percentage
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun agentOxygenIsBelow(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("agentOxygenIsBelow($percentage)")
            .toSolve { belief: SeAgentState ->
                return@toSolve belief.seEnv.controller.observer.observe().oxygen * 100 < percentage
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun agentOxygenIsAbove(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("agentOxygenIsAbove($percentage)")
            .toSolve { belief: SeAgentState ->
                return@toSolve belief.seEnv.controller.observer.observe().oxygen * 100 > percentage
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun agentEnergyIsBelow(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("agentEnergyIsBelow($percentage)")
            .toSolve { belief: SeAgentState ->
                return@toSolve belief.seEnv.controller.observer.observe().energy * 100 < percentage
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun agentEnergyIsAbove(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("agentEnergyIsAbove($percentage)")
            .toSolve { belief: SeAgentState ->
                return@toSolve belief.seEnv.controller.observer.observe().energy * 100 > percentage
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun blockOfTypeExists(blockType: String, tactic: Tactic = tactics.doNothing()): GoalStructure {
        return Goal("Block of type $blockType exists")
            .toSolve { belief: SeAgentState ->
                belief.seEnv.observeForNewBlocks()
                belief.seEnv.context.allNewestBlocks.any { it.definitionId.type == blockType }
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun alwaysSolved(tactic: Tactic = tactics.doNothing()): GoalStructure {
        return Goal("alwaysSolved")
            .toSolve { belief: SeAgentState ->
                true
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun lastBuiltBlockIntegrityIsAbove(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return lastBuiltBlockIntegrityCheck(
            percentage = percentage,
            checkFunction = blockIntegrityIsAbove(percentage),
            tactic = tactic
        )
    }

    fun lastBuiltBlockIntegrityIsBelow(
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return lastBuiltBlockIntegrityCheck(
            percentage = percentage,
            checkFunction = blockIntegrityIsBelow(percentage),
            tactic = tactic
        )
    }

    private fun lastBuiltBlockIntegrityCheck(
        percentage: Double,
        checkFunction: (Block) -> Boolean,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("lastBuiltBlockIsAtPercentageIntegrity($percentage)")
            .toSolve { belief: SeAgentState ->
                belief.seEnv.run {
                    controller.observer.observeBlocks().allBlocks.find { context.lastNewBlock?.id == it.id }
                        ?.let { foundBlock ->
                            checkFunction(foundBlock)
                        } ?: false
                }
            }
            .withTactic(
                tactic
            ).lift()
    }

    fun typeBuiltBlockIntegrityIsAbove(
        blockType: String,
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return typeBuiltBlockIntegrityCheck(
            blockType,
            percentage = percentage,
            checkFunction = blockIntegrityIsAbove(percentage),
            tactic = tactic
        )
    }

    fun typeBuiltBlockIntegrityIsBelow(
        blockType: String,
        percentage: Double,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return typeBuiltBlockIntegrityCheck(
            blockType,
            percentage = percentage,
            checkFunction = blockIntegrityIsBelow(percentage),
            tactic = tactic
        )
    }

    private fun typeBuiltBlockIntegrityCheck(
        blockType: String,
        percentage: Double,
        checkFunction: (Block) -> Boolean,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("typeBuiltBlockIntegrityCheck($percentage)")
            .toSolve { belief: SeAgentState ->
                belief.seEnv.run {
                    belief.seEnv.controller.observer.observeBlocks().allBlocks.find { blockType in it.definitionId.toString() }
                        ?.let { foundBlock ->
                            checkFunction(foundBlock)
                        } ?: false
                }
            }
            .withTactic(
                tactic
            ).lift()
    }

    private fun blockIntegrityIsAbove(percentage: Double): (Block) -> Boolean {
        return fun(block): Boolean {
            val requiredIntegrity = block.maxIntegrity * percentage
            return block.integrity >= requiredIntegrity
        }
    }

    private fun blockIntegrityIsBelow(percentage: Double): (Block) -> Boolean {
        return fun(block): Boolean {
            val requiredIntegrity = block.maxIntegrity * percentage
            return block.integrity <= requiredIntegrity
        }
    }

    fun terminalIsOpened(
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure.PrimitiveGoal {
        val goal = Goal("terminalIsOpened()")
            .toSolve { belief: SeAgentState ->
                return@toSolve belief.seEnv.controller.screens.terminal.inventory != null
            }
            .withTactic(
                tactic
            )
        return goal.lift()
    }

    fun terminalContainsElement(
        element: String,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure.PrimitiveGoal {
        val goal = Goal("terminalContainsElement($element)")
            .toSolve { belief: SeAgentState ->
                if (belief.seEnv.controller.screens.terminal.inventory != null) {
                    var inventory = belief.seEnv.controller.screens.terminal.inventory.data()
                    inventory.rightInventories.forEach { inv ->
                        inv.items.forEach { item ->
                            if (item.id.toString().contains(element)) {
                                return@toSolve true
                            }
                        }
                    }
                }
                return@toSolve false
            }
            .withTactic(
                tactic
            )
        return goal.lift()
    }

    fun inventoryContainsItem(
        itemName: String,
        numberElements: Int = 1,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure.PrimitiveGoal {
        val goal = Goal("inventoryContainsItem($itemName) with numberElements($numberElements)")
            .toSolve { belief: SeAgentState ->
                // Open the inventory to enable the access to the elements
                Thread.sleep(500)
                belief.seEnv.controller.character.showInventory()
                Thread.sleep(500)
                belief.seEnv.controller.observer.observe()

                // Check if the inventory contains the element
                var inventory = belief.seEnv.controller.screens.terminal.inventory.data()
                inventory.leftInventories.forEach { inv ->
                    inv.items.forEach { item ->
                        if (item.id.toString().contains(itemName) && item.amount >= numberElements) {
                            // Element found, close the inventory terminal and accept goal solved
                            belief.seEnv.controller.screens.terminal.close()
                            Thread.sleep(500)
                            return@toSolve true
                        }
                    }
                }

                // Element not found, close the inventory terminal and discard goal solved
                belief.seEnv.controller.screens.terminal.close()
                Thread.sleep(500)
                return@toSolve false
            }
            .withTactic(
                tactic
            )
        return goal.lift()
    }

    fun inventoryDoesNotContainItem(
        itemName: String,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure.PrimitiveGoal {
        val goal = Goal("inventoryDoesNotContainItem($itemName)")
            .toSolve { belief: SeAgentState ->
                // Open the inventory to enable access to the elements
                Thread.sleep(500)
                belief.seEnv.controller.character.showInventory()
                Thread.sleep(500)
                belief.seEnv.controller.observer.observe()

                // Assume the item is not found initially
                var itemFound = false

                // Check the entire inventory
                val inventory = belief.seEnv.controller.screens.terminal.inventory.data()
                inventory.leftInventories.forEach { inv ->
                    inv.items.forEach { item ->
                        if (item.id.toString().contains(itemName)) {
                            // Mark item as found
                            itemFound = true
                        }
                    }
                }

                // Close the inventory terminal
                belief.seEnv.controller.screens.terminal.close()
                Thread.sleep(500)

                // If item was found, the goal is not solved
                return@toSolve !itemFound
            }
            .withTactic(
                tactic
            )
        return goal.lift()
    }

    fun aimToBlock(
        blockType: String,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure.PrimitiveGoal {
        val goal =
            Goal("aimToBlock($blockType)")
                .toSolve { belief: SeAgentState ->
                    val targetBlock = belief.seEnv.controller.observer.observe().targetBlock
                    if (targetBlock != null) {
                        println("targetBlock $targetBlock")
                        if (blockType in targetBlock.definitionId.toString()) {
                            // Agent is aiming the block
                            return@toSolve true
                        }
                    }
                    // targetBlock not found or agent not aiming the targetBlock
                    false
                }
                .withTactic(
                    tactic
                )
        return goal.lift()
    }

    fun navigateNearToBlock(
        blockType: String,
        goalDistance: Float = 3.2f,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure.PrimitiveGoal {
        val goal =
            Goal("navigateNearToBlock($blockType)")
                .toSolve { belief: SeAgentState ->
                    // Get all blocks matching the block type
                    val matchingBlocks = belief.seEnv.controller.observer.observeBlocks().allBlocks.filter {
                        it.definitionId.toString().contains(blockType)
                    }

                    // Check if the agent is near any of the matching blocks
                    for (block in matchingBlocks) {
                        val blockPosition = block.position
                        val agentIsNearBlock = blockPosition != null &&
                            belief.seEnv.controller.observer.distanceTo(blockPosition) < goalDistance
                        if (agentIsNearBlock) {
                            // Agent is near one of the blocks
                            return@toSolve true
                        }
                    }

                    // No matching block is within the goal distance
                    false
                }
                .withTactic(
                    tactic
                )
        return goal.lift()
    }

    fun navigateAimToBlock(
        blockType: String,
        goalDistance: Float = 3.2f,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        val navigateBlockGoal = navigateNearToBlock(blockType, goalDistance, tactic)
        val aimBlockGoal = aimToBlock(blockType, tactic)
        return SEQ(navigateBlockGoal, aimBlockGoal)
    }
}
