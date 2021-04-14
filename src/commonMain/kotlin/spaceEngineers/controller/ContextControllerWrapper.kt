package spaceEngineers.controller

import spaceEngineers.model.SeObservation

class ContextControllerWrapper(
    val spaceEngineers: SpaceEngineers,
    val context: SpaceEngineersTestContext = SpaceEngineersTestContext()
) : SpaceEngineers {


    private fun addToHistory(observation: SeObservation) {
        context.addToHistory(observation)
    }


    override val session: Session = spaceEngineers.session

    override val character: Character = spaceEngineers.character

    override val items: Items = spaceEngineers.items

    override val observer: Observer = object: Observer {
        override fun observe(): SeObservation {
            return spaceEngineers.observer.observe().apply(::addToHistory)
        }

        override fun observeBlocks(): SeObservation {
            return spaceEngineers.observer.observeBlocks().apply(::addToHistory)
        }

        override fun observeNewBlocks(): SeObservation {
            return spaceEngineers.observer.observeNewBlocks().apply(::addToHistory)
        }

        override fun observeEntities(): SeObservation {
            return spaceEngineers.observer.observeEntities().apply(::addToHistory)
        }
    }
}