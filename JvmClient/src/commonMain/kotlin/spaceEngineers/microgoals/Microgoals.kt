package spaceEngineers.microgoals

import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.Observation
import spaceEngineers.model.extensions.blockById


suspend fun SpaceEngineers.runUntilObservationGoal(goalCheck: SpaceEngineersGoal, timeoutMs: Long) {
    withTimeout(timeoutMs) {
        while (!goalCheck.accomplished()) {
            goalCheck.update(this@runUntilObservationGoal)
            yield()
        }
    }
}

interface SpaceEngineersGoal {
    fun update(spaceEngineers: SpaceEngineers)

    fun accomplished(): Boolean
}

open class BlockObservationGoal(val checkObservation: (Observation) -> Boolean) : SpaceEngineersGoal {
    var observation: Observation? = null
    override fun update(spaceEngineers: SpaceEngineers) {
        observation = spaceEngineers.observer.observeBlocks()
    }


    override fun accomplished(): Boolean {
        return observation?.let { checkObservation(it) } ?: false
    }

}

class BlockIntegrityAtLeastGoal(blockId: String, val integrity: Double) :
    BlockObservationGoal({ it.blockById(blockId).integrity >= integrity })

class BlockIntegrityAtMostGoal(blockId: String, val integrity: Double) :
    BlockObservationGoal({ it.blockById(blockId).integrity <= integrity })
