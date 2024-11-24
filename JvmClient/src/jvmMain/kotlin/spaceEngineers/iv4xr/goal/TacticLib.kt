package spaceEngineers.iv4xr.goal

import environments.SeAgentState
import kotlinx.coroutines.runBlocking
import nl.uu.cs.aplib.AplibEDSL
import nl.uu.cs.aplib.mainConcepts.Tactic
import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.iv4xr.navigation.NavigableSystem
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3F

class TacticLib {

    fun doNothing(): Tactic {
        return AplibEDSL.action("doNothing").do1<SeAgentState, Any> { belief: SeAgentState ->
            belief
        }.lift()
    }

    fun buildBlock(blockType: String): Tactic {
        return AplibEDSL.action("buildBlock($blockType)").do1<SeAgentState, Any> { belief: SeAgentState ->
            belief.apply { seEnv.equipAndPlace(blockType) }
        }.lift()
    }

    fun buildAssemblerBlock(blockType: String): Tactic {
        return AplibEDSL.action("buildAssemblerBlock($blockType)").do1<SeAgentState, Any> { belief: SeAgentState ->
            belief.apply {
                val block = DefinitionId.assembler(blockType)
                val toolbarLocation = ToolbarLocation(1, 0)
                seEnv.context.blockTypeToToolbarLocation[block.type] = toolbarLocation
                seEnv.controller.items.setToolbarItem(block, toolbarLocation)
                seEnv.equipAndPlace(blockType)
            }
        }.lift()
    }

    fun observeNewBlocks(): Tactic {
        return AplibEDSL.action("observeNewBlocks").do1 { belief: SeAgentState ->
            belief.apply { seEnv.observeForNewBlocks() }
        }.lift()
    }

    fun observe(): Tactic {
        return AplibEDSL.action("observe").do1 { belief: SeAgentState ->
            belief.apply { observe() }
        }.lift()
    }

    fun moveForward(): Tactic {
        return AplibEDSL.action("moveForward").do1 { belief: SeAgentState ->
            belief.apply {
                seEnv.moveForward()
            }
        }.lift()
    }

    fun equip(toolbarLocation: ToolbarLocation): Tactic {
        return AplibEDSL.action("equip($toolbarLocation)").do1 { belief: SeAgentState ->
            belief.apply { seEnv.equip(toolbarLocation) }
        }.lift()
    }

    fun equip(tool: String): Tactic {
        return AplibEDSL.action("equip($tool)").do1 { belief: SeAgentState ->
            belief.apply {
                val toolId = DefinitionId.physicalGun(tool)
                val toolbarLocation = ToolbarLocation(1, 0)
                seEnv.controller.items.setToolbarItem(toolId, toolbarLocation)
                seEnv.equip(toolbarLocation)
            }
        }.lift()
    }

    fun sleep(millis: Long): Tactic {
        return AplibEDSL.action("sleep($millis)").do1 { belief: SeAgentState ->
            belief.apply {
                Thread.sleep(millis)
            }
        }.lift()
    }

    fun startUsingTool(): Tactic {
        return AplibEDSL.action("startUsingTool()").do1 { belief: SeAgentState ->
            belief.apply { seEnv.beginUsingTool() }
        }.lift()
    }

    fun endUsingTool(): Tactic {
        return AplibEDSL.action("endUsingTool()").do1 { belief: SeAgentState ->
            belief.apply { seEnv.endUsingTool() }
        }.lift()
    }

    fun useToolTime(
        waitMillis: Long = 0
    ): Tactic {
        return AplibEDSL.action("useToolTime()").do1 { belief: SeAgentState ->
            belief.apply {
                seEnv.beginUsingTool()
                Thread.sleep(waitMillis)
                seEnv.endUsingTool()
            }
        }.lift()
    }

    fun use(
        waitMillis: Long = 0
    ): Tactic {
        return AplibEDSL.action("use()").do1 { belief: SeAgentState ->
            belief.apply {
                seEnv.use()
                Thread.sleep(waitMillis)
            }
        }.lift()
    }

    fun continuousUse(
        useMillis: Long,
        waitMillis: Long = 0
    ): Tactic {
        return AplibEDSL.action("continuousUse($useMillis)").do1 { belief: SeAgentState ->
            belief.apply {

                val startTime = System.currentTimeMillis()
                var elapsedTime = 0L

                while (elapsedTime < useMillis) {
                    seEnv.use()
                    elapsedTime = System.currentTimeMillis() - startTime
                }

                Thread.sleep(waitMillis)
            }
        }.lift()
    }

    fun dropItem(
        itemName: String
    ): Tactic {
        return AplibEDSL.action("dropItem($itemName)").do1 { belief: SeAgentState ->
            belief.apply {
                // Open the inventory to enable the access to the elements
                Thread.sleep(500)
                belief.seEnv.controller.character.showInventory()
                Thread.sleep(500)
                belief.seEnv.controller.observer.observe()

                // Check if the inventory contains the element
                var inventory = belief.seEnv.controller.screens.terminal.inventory.data()
                inventory.leftInventories.forEach { inv ->
                    inv.items.forEach { item ->
                        println("item($item)")
                        if (item.id.toString().contains(itemName)) {
                            // Select and drop the match item, handling exceptions
                            // I think there is a BUG in the SE plugin
                            // The itemId is increasing after first usage instead of restart is position count
                            // This provokes second time the item is found the itemId is wrong
                            try {
                                Thread.sleep(500)
                                belief.seEnv.controller.screens.terminal.inventory.left.selectItem(item.itemId)
                                Thread.sleep(500)
                                belief.seEnv.controller.screens.terminal.inventory.dropSelected()
                                Thread.sleep(500)
                            } catch (e: Exception) {
                                println("Failed to drop item: ${item.itemId}, error: ${e.message}")
                            }
                        }
                    }
                }

                // Element found, close the inventory terminal
                belief.seEnv.controller.screens.terminal.close()
                Thread.sleep(500)
            }
        }.lift()
    }

    fun cleanInventoryItems(
        inventoryPos: Int = 5
    ): Tactic {
        return AplibEDSL.action("cleanInventoryItems()").do1 { belief: SeAgentState ->
            belief.apply {
                // Open the inventory to enable the access to the elements
                Thread.sleep(500)
                belief.seEnv.controller.character.showInventory()
                Thread.sleep(500)
                belief.seEnv.controller.observer.observe()

                // Adjusted logic to repeatedly remove selected item until there are no items
                var inventory = belief.seEnv.controller.screens.terminal.inventory.data()
                var itemRemoved: Boolean
                do {
                    itemRemoved = false
                    inventory.leftInventories.forEach { inv ->
                        val item = inv.items.find { it.itemId > inventoryPos }
                        if (item != null) {
                            try {
                                println("Attempting to remove item(${item.itemId})")
                                Thread.sleep(500)
                                belief.seEnv.controller.screens.terminal.inventory.left.selectItem(inventoryPos + 1)
                                Thread.sleep(500)
                                belief.seEnv.controller.screens.terminal.inventory.dropSelected()
                                Thread.sleep(500)
                                itemRemoved = true
                            } catch (e: Exception) {
                                println("Failed to drop item: ${item.itemId}, error: ${e.message}")
                            }
                        }
                    }
                    // Refresh inventory after each removal
                    belief.seEnv.controller.observer.observe()
                    inventory = belief.seEnv.controller.screens.terminal.inventory.data()
                } while (itemRemoved)

                // Close the inventory terminal
                belief.seEnv.controller.screens.terminal.close()
                Thread.sleep(500)
            }
        }.lift()
    }

    fun setHelmet(
        enabled: Boolean,
        waitMillis: Long = 0
    ): Tactic {
        return AplibEDSL.action("setHelmet($enabled)").do1 { belief: SeAgentState ->
            belief.apply {
                seEnv.setHelmet(enabled)
                Thread.sleep(waitMillis)
            }
        }.lift()
    }

    fun closeTerminal(): Tactic {
        return AplibEDSL.action("closeTerminal()").do1 { belief: SeAgentState ->
            belief.apply { seEnv.closeTerminal() }
        }.lift()
    }

    fun rotateMilliseconds(
        milliseconds: Long
    ): Tactic {
        return AplibEDSL.action("rotateMilliseconds($milliseconds)").do1 { belief: SeAgentState ->
            belief.apply {
                val navigableSystem = NavigableSystem(seEnv.controller, seEnv.controller.observer)

                runBlocking {
                    navigableSystem.rotateMilliseconds(milliseconds)
                }
            }
        }.lift()
    }

    fun rotateToBlock(
        desiredBlock: String
    ): Tactic {
        return AplibEDSL.action("rotateToBlock($desiredBlock)").do1 { belief: SeAgentState ->
            belief.apply {
                val navigableSystem = NavigableSystem(seEnv.controller, seEnv.controller.observer)

                runBlocking {
                    navigableSystem.rotateToBlock(desiredBlock)
                }
            }
        }.lift()
    }

    /**
     * Navigation tactic on flat plane or ground levels.
     * NavigableSystem calculates and follows a path with a fixed Y-axis.
     */
    fun navigateToBlock(
        desiredBlock: String,
        closestDistance: Float = 3f,
        movementType: CharacterMovementType = CharacterMovementType.RUN,
        distancePathTolerance: Float = 1.2f
    ): Tactic {
        return AplibEDSL.action("navigateToBlock($desiredBlock)").do1 { belief: SeAgentState ->
            belief.apply {
                val navigableSystem = NavigableSystem(seEnv.controller, seEnv.controller.observer)
                val blockPosition = navigableSystem.setDesiredBlockPosition(desiredBlock)

                println("navigateToBlock($desiredBlock): agentPosition is (${seEnv.controller.observer.observe().position})")

                if (blockPosition == Vec3F(0, 0, 0)) {
                    println("navigateToBlock($desiredBlock): blockPosition not found")
                    doNothing()
                } else {
                    println("navigateToBlock($desiredBlock): blockPosition is ($blockPosition)")
                    println("navigateToBlock($desiredBlock): distance is (${seEnv.controller.observer.distanceTo(blockPosition)})")

                    val navigableGraph = navigableSystem.getNavigableGraph()
                    val navigablePath = navigableSystem.getClosestPathToDesiredBlock(closestDistance)

                    runBlocking {
                        navigableSystem.navigatePath(
                            navigableGraph = navigableGraph,
                            navigablePath = navigablePath,
                            movementType = movementType,
                            distancePathTolerance = distancePathTolerance
                        )
                    }
                }
            }
        }.lift()
    }

    /**
     * Navigation tactic on dynamic tridimensional levels.
     * NavigableSystem re-calculates the path on levels with dynamic Y-axis.
     */
    fun dynamicNavigationNearToBlock(
        desiredBlock: String,
        distancePathTolerance: Float = 1.2f,
    ): Tactic {
        return AplibEDSL.action("dynamicNavigationNearToBlock($desiredBlock)").do1 { belief: SeAgentState ->
            belief.apply {
                val navigableSystem = NavigableSystem(seEnv.controller, seEnv.controller.observer)
                val blockPosition = navigableSystem.setDesiredBlockPosition(desiredBlock)

                println("dynamicNavigationNearToBlock($desiredBlock): agentPosition is (${seEnv.controller.observer.observe().position})")

                if (blockPosition == Vec3F(0, 0, 0)) {
                    println("dynamicNavigationNearToBlock($desiredBlock): blockPosition not found")
                    doNothing()
                } else {
                    println("dynamicNavigationNearToBlock($desiredBlock): blockPosition is ($blockPosition)")
                    println("dynamicNavigationNearToBlock($desiredBlock): distance is (${seEnv.controller.observer.distanceTo(blockPosition)})")

                    runBlocking {
                        navigableSystem.navigateDynamicPath(navigableSystem, distancePathTolerance)
                    }
                }
            }
        }.lift()
    }
}
