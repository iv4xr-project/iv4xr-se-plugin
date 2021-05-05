package spaceEngineers.controller

import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.InteractionType
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.model.*
import java.io.Closeable

class OldProtocolSpaceEngineers(val controller: ProprietaryJsonTcpCharacterController) : SpaceEngineers, Closeable {
    override val session: Session = object : Session {
        override fun loadScenario(scenarioPath: String) {
            controller.loadScenario(scenarioPath)
        }
    }

    override val character: Character = object : Character {
        override fun moveAndRotate(movement: Vec3, rotation3: Vec2, roll: Float): Observation {
            controller.moveAndRotate(MovementArgs(movement, rotation3, roll))
            return observer.observe()
        }

        override fun teleport(position: Vec3): Observation {
            TODO("Not yet implemented")
        }
    }

    override val items: Items = object : Items {
        override fun place() {
            controller.interact(InteractionArgs(InteractionType.PLACE))
        }

        override fun equip(toolbarLocation: ToolbarLocation) {
            controller.interact(InteractionArgs.equip(toolbarLocation = toolbarLocation))
        }

        override fun beginUsingTool() {
            controller.interact(InteractionArgs(InteractionType.BEGIN_USE))
        }

        override fun endUsingTool() {
            controller.interact(InteractionArgs(InteractionType.END_USE))
        }

        override fun setToolbarItem(name: String, toolbarLocation: ToolbarLocation) {
            controller.interact(
                InteractionArgs(
                    InteractionType.TOOLBAR_SET,
                    slot = toolbarLocation.slot,
                    page = toolbarLocation.page,
                    name
                )
            )
        }

        override fun getToolbar(): Toolbar {
            TODO("Not yet implemented")
        }
    }

    override val observer: Observer = object : Observer {
        override fun observe(): Observation {
            return controller.observe(ObservationMode.DEFAULT)
        }

        override fun observeBlocks(): Observation {
            return controller.observe(ObservationMode.BLOCKS)
        }

        override fun observeNewBlocks(): Observation {
            return controller.observe(ObservationMode.NEW_BLOCKS)
        }

        override fun takeScreenshot(absolutePath: String) {
            TODO("Not yet implemented")
        }
    }
    override val definitions: Definitions
        get() = TODO("Not yet implemented")

    override fun close() {
        controller.close()
    }

    companion object {
        fun localhost(agentId: String): OldProtocolSpaceEngineers {
            return OldProtocolSpaceEngineers(
                ProprietaryJsonTcpCharacterController.localhost(agentId)
            )
        }
    }
}
