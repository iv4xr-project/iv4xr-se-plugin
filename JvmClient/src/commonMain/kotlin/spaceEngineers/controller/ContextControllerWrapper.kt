package spaceEngineers.controller

import spaceEngineers.model.Observation
import spaceEngineers.model.allBlocks

class ContextControllerWrapper(
    val spaceEngineers: SpaceEngineers,
    val context: SpaceEngineersTestContext = SpaceEngineersTestContext()
) : SpaceEngineers {


    private fun addToHistory(observation: Observation) {
        context.addToHistory(observation)
    }

    override val session: Session = spaceEngineers.session

    override val character: Character = spaceEngineers.character

    override val items: Items = spaceEngineers.items

    override val observer: Observer = object: Observer {
        override fun observe(): Observation {
            return spaceEngineers.observer.observe().apply(::addToHistory)
        }

        override fun observeBlocks(): Observation {
            return spaceEngineers.observer.observeBlocks().apply(::addToHistory).apply {
                context.updatePlatformOrientationUpIfNotSet(this)
            }
        }

        override fun observeNewBlocks(): Observation {
            return spaceEngineers.observer.observeNewBlocks().apply {
                context.updateNewBlocks(allBlocks)
            }.apply(::addToHistory)
        }

        override fun takeScreenshot(absolutePath: String) {
            spaceEngineers.observer.takeScreenshot(absolutePath)
        }
    }
    override val definitions: Definitions = spaceEngineers.definitions

}
