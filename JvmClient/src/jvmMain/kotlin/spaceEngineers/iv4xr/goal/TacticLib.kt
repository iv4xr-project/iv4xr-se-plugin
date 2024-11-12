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
        closestDistance: Float,
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
                        navigableSystem.navigatePath(navigableGraph, navigablePath, distancePathTolerance)
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
