package spaceEngineers.iv4xr.goal

import environments.SeAgentState
import nl.uu.cs.aplib.AplibEDSL
import nl.uu.cs.aplib.mainConcepts.Tactic
import spaceEngineers.model.ToolbarLocation

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
}
