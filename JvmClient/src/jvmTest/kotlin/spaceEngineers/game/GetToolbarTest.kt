package spaceEngineers.game

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.game.mockable.filterForScreenshots
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.extensions.allBlocks
import testhelp.MockOrRealGameTest
import java.lang.Thread.sleep
import kotlin.test.Test
import kotlin.test.assertEquals


fun SpaceEngineers.checkBlockType(
    blockType: String,
    location: ToolbarLocation = ToolbarLocation(0, 0)
): Boolean {
    items.setToolbarItem(blockType, location)
    sleep(10)
    val toolbar = items.getToolbar()
    sleep(10)
    val toolbarItem = toolbar[location]
    return toolbarItem != null && toolbarItem.subType == blockType
}

fun SpaceEngineers.checkPlacement(
    blockType: String,
    location: ToolbarLocation = ToolbarLocation(0, 0)
): Boolean {
    observer.observeNewBlocks()
    sleep(10)
    items.equip(location)
    sleep(10)
    blocks.place()
    sleep(10)
    return observer.observeNewBlocks().allBlocks.let { it.isNotEmpty() && it.first().definitionId.type == blockType }
}


class GetToolbarTest : MockOrRealGameTest(forceRealGame = true) {


    @Test
    fun getToolbar() = testContext {
        val toolbar = items.getToolbar()
        assertEquals(toolbar[ToolbarLocation(0, 0)]?.subType, "LargeBlockArmorBlock")
        assertEquals(toolbar.items.size, toolbar.pageCount * toolbar.slotCount)
    }

    @Test
    fun setToolbar() = testContext {
        val blockTypes = definitions.blockDefinitions().filterForScreenshots().map { it.definitionId.type }

        val location = ToolbarLocation(0, 2)
        val (success, fail) = blockTypes.partition { blockType ->
            sleep(10)
            session.loadFromTestResources("simple-place-grind-torch")
            sleep(10)
            (checkBlockType(blockType, location) && checkPlacement(blockType, location)).apply {
                if (this) {
                    println("${blockType}")
                }
            }
        }
        println("success: ${success.size}")
        println(success.joinToString("\n") { blockType ->
            "| ${blockType} |"
        })
        println("fail: ${fail.size}")
        println(fail.joinToString("\n") { blockType ->
            "| ${blockType} |"
        })

    }

    @Test
    fun equipSmall() = testContext {
        val location = ToolbarLocation(2, 1)
        observer.observeNewBlocks()
        val blockType = "SmallHeavyBlockArmorBlock"
        items.setToolbarItem(blockType, location)
        items.equip(location)
        blocks.place()
        val blocks = observer.observeNewBlocks().allBlocks
        assertEquals(1, blocks.size)
        val block = blocks.first()
        assertEquals(blockType, block.definitionId.type)
    }
}
