package spaceEngineers.iv4xr.goal

import environments.SeAgentState
import kotlinx.coroutines.runBlocking
import nl.uu.cs.aplib.AplibEDSL
import nl.uu.cs.aplib.mainConcepts.Tactic
import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.iv4xr.navigation.NavigableSystem
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

    fun interact(): Tactic {
        return AplibEDSL.action("interact()").do1 { belief: SeAgentState ->
            belief.apply { seEnv.interact() }
        }.lift()
    }

    fun closeTerminal(): Tactic {
        return AplibEDSL.action("closeTerminal()").do1 { belief: SeAgentState ->
            belief.apply { seEnv.closeTerminal() }
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
    fun groundedNavigationNearToBlock(
        desiredBlock: String,
        closestDistance: Float
    ): Tactic {
        return AplibEDSL.action("groundedNavigationNearToBlock($desiredBlock)").do1 { belief: SeAgentState ->
            belief.apply {
                val navigableSystem = NavigableSystem(seEnv.controller, seEnv.controller.observer)
                val blockPosition = navigableSystem.setDesiredBlockPosition(desiredBlock)

                println("groundedNavigationNearToBlock($desiredBlock): agentPosition is (${seEnv.controller.observer.observe().position})")

                if (blockPosition == Vec3F(0, 0, 0)) {
                    println("groundedNavigationNearToBlock($desiredBlock): blockPosition not found")
                    doNothing()
                } else {
                    println("groundedNavigationNearToBlock($desiredBlock): blockPosition is ($blockPosition)")
                    println("groundedNavigationNearToBlock($desiredBlock): distance is (${seEnv.controller.observer.distanceTo(blockPosition)})")

                    val navigableGraph = navigableSystem.getNavigableGraph()
                    val navigablePath = navigableSystem.getClosestPathToDesiredBlock(closestDistance)

                    runBlocking {
                        navigableSystem.navigateGroundedPath(navigableGraph, navigablePath)
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
        closestDistance: Float
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
                        navigableSystem.navigateDynamicPath(navigableSystem, closestDistance)
                    }
                }
            }
        }.lift()
    }
}
