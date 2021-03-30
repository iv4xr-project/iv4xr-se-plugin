package spaceEngineers.iv4xr.goal

import environments.SeAgentState
import eu.iv4xr.framework.mainConcepts.W3DAgentState
import eu.iv4xr.framework.spatial.Vec3
import nl.uu.cs.aplib.mainConcepts.Goal
import nl.uu.cs.aplib.mainConcepts.GoalStructure
import nl.uu.cs.aplib.mainConcepts.Tactic
import spaceEngineers.model.SeBlock
import spaceEngineers.model.allBlocks
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
                .toSolve { belief: W3DAgentState ->
                    abs(Vec3.dist(belief.wom.position, position) - distance) < epsilon
                }
                .withTactic(
                    tactic
                )
        return goal.lift()
    }

    fun blockOfTypeExists(blockType: String, tactic: Tactic = tactics.doNothing()): GoalStructure {
        return Goal("Block of type $blockType exists")
            .toSolve { belief: SeAgentState ->
                belief.seEnv.observeForNewBlocks()
                belief.seEnv.context.allNewestBlocks.any { it.blockType == blockType }
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
        checkFunction: (SeBlock) -> Boolean,
        tactic: Tactic = tactics.doNothing()
    ): GoalStructure {
        Thread.sleep(500)
        return Goal("lastBuiltBlockIsAtPercentageIntegrity($percentage)")
            .toSolve { belief: SeAgentState ->
                belief.seEnv.run {
                    controller.observer.observeBlocks().allBlocks.find { context.lastNewBlockId == it.id }
                        ?.let { foundBlock ->
                            checkFunction(foundBlock)
                        } ?: false
                }
            }
            .withTactic(
                tactic
            ).lift()
    }

    private fun blockIntegrityIsAbove(percentage: Double): (SeBlock) -> Boolean {
        return fun(block): Boolean {
            val requiredIntegrity = block.maxIntegrity * percentage
            return block.integrity >= requiredIntegrity
        }
    }

    private fun blockIntegrityIsBelow(percentage: Double): (SeBlock) -> Boolean {
        return fun(block): Boolean {
            val requiredIntegrity = block.maxIntegrity * percentage
            return block.integrity <= requiredIntegrity
        }
    }



}