package spaceEngineers.controller

import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.ExtendedEntity
import spaceEngineers.model.FloatingObject
import spaceEngineers.model.Observation
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.navigation.NavGraph

class ContextControllerWrapper(
    val spaceEngineers: SpaceEngineers,
    val context: SpaceEngineersTestContext = SpaceEngineersTestContext()
) : SpaceEngineers {


    private fun addToHistory(observation: Observation) {
        context.addToHistory(observation)
    }

    private fun addToHistory(observation: CharacterObservation) {
        context.addToHistory(observation)
    }

    override val session: Session = spaceEngineers.session

    override val character: Character = spaceEngineers.character

    override val items: Items = spaceEngineers.items

    override val observer: Observer = object: Observer {
        override fun observe(): CharacterObservation {
            return spaceEngineers.observer.observe().apply(::addToHistory)
        }

        override fun observeControlledEntity(): ExtendedEntity {
            return spaceEngineers.observer.observeControlledEntity()
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

        override fun observeCharacters(): List<CharacterObservation> {
            return spaceEngineers.observer.observeCharacters()
        }

        override fun observeFloatingObjects(): List<FloatingObject> {
            return spaceEngineers.observer.observeFloatingObjects()
        }

        override fun navigationGraph(gridId: String): NavGraph {
            return spaceEngineers.observer.navigationGraph(gridId)
        }

        override fun takeScreenshot(absolutePath: String) {
            spaceEngineers.observer.takeScreenshot(absolutePath)
        }

        override fun switchCamera() {
            spaceEngineers.observer.switchCamera()
        }
    }
    override val definitions: Definitions = spaceEngineers.definitions
    override val blocks: Blocks = spaceEngineers.blocks
    override val admin: SpaceEngineersAdmin = spaceEngineers.admin
    override val screens: Screens = spaceEngineers.screens
    override val input: Input = spaceEngineers.input

}
