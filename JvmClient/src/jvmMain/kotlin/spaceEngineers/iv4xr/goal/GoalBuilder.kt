package spaceEngineers.iv4xr.goal

import environments.SeAgentState
import eu.iv4xr.framework.spatial.Vec3
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
        return Goal("lastBuiltBlockIsAtPercentageIntegrity($percentage)")
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
        distance: Float,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure.PrimitiveGoal {
        val goal =
            Goal("navigateNearToBlock($blockType)")
                .toSolve { belief: SeAgentState ->
                    val foundBlock = belief.seEnv.controller.observer.observeBlocks().allBlocks.find {
                        it.definitionId.toString().contains(blockType)
                    }
                    val blockPosition = foundBlock?.position
                    if (blockPosition != null) {
                        val agentIsNearBlock = belief.seEnv.controller.observer.distanceTo(blockPosition) < distance
                        if (agentIsNearBlock) {
                            // Agent is near the block
                            return@toSolve true
                        }
                    }
                    // Block not found or not close enough
                    false
                }
                .withTactic(
                    tactic
                )
        return goal.lift()
    }
}
