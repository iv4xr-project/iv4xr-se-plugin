package spaceEngineers.controller

import environments.closeIfCloseable
import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.InteractionType
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.model.SeObservation
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3
import java.io.Closeable

class OldProtocolSpaceEngineers(val controller: ProprietaryJsonTcpCharacterController) : SpaceEngineers, Closeable {
    override val session: Session = object : Session {
        override fun load(id: String) {
            controller.load(id)
        }
    }

    override val character: Character = object : Character {
        override fun moveAndRotate(movement: Vec3, rotation3: Vec3, roll: Float): SeObservation {
            controller.moveAndRotate(MovementArgs(movement, rotation3, roll))
            return observer.observe()
        }
    }

    override val items: Items = object : Items {
        override fun place() {
            controller.interact(InteractionArgs(InteractionType.PLACE))
        }

        override fun equip(toolbarLocation: ToolbarLocation) {
            controller.interact(InteractionArgs.equip(toolbarLocation = toolbarLocation))
        }

        override fun startUsingTool() {
            controller.interact(InteractionArgs(InteractionType.BEGIN_USE))
        }

        override fun endUsingTool() {
            controller.interact(InteractionArgs(InteractionType.END_USE))
        }
    }

    override val observer: Observer = object : Observer {
        override fun observe(): SeObservation {
            return controller.observe(ObservationMode.DEFAULT)
        }

        override fun observeBlocks(): SeObservation {
            return controller.observe(ObservationMode.BLOCKS)
        }

        override fun observeNewBlocks(): SeObservation {
            return controller.observe(ObservationMode.NEW_BLOCKS)
        }

        override fun observeEntities(): SeObservation {
            return controller.observe(ObservationMode.ENTITIES)
        }
    }

    override fun close() {
        controller.close()
    }
}