package spaceEngineers.game

import org.junit.jupiter.api.Disabled
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.game.mockable.filterForScreenshots
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.extensions.allBlocks
import testhelp.MockOrRealGameTest
import java.lang.Thread.sleep
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


fun SpaceEngineers.checkBlockType(
    blockType: String,
    location: ToolbarLocation = ToolbarLocation(0, 0)
): Boolean {
    items.setToolbarItem(blockType, location)
    sleep(10)
    val toolbar = items.toolbar()
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
    return (observer.observeNewBlocks().allBlocks.let { it.isNotEmpty() && it.first().definitionId.type == blockType }).also {
        observer.observeBlocks().allBlocks.forEach {
            admin.blocks.remove(it.id)
        }
    }
}


class GetToolbarTest : MockOrRealGameTest() {


    @Test
    fun toolbar() = testContext {
        val toolbar = items.toolbar()
        assertEquals(toolbar[ToolbarLocation(0, 0)]?.subType, "LargeBlockArmorBlock")
        assertEquals(toolbar.items.size, toolbar.pageCount * toolbar.slotCount)
    }

    @Test
    fun setToolbar() = testContext {
        val blockTypes = definitions.blockDefinitions().filterForScreenshots().map { it.definitionId.type }

        val location = ToolbarLocation(0, 2)
        val (success, fail) = blockTypes.partition { blockType ->
            delay(10)
            (checkBlockType(blockType, location) && checkPlacement(blockType, location))
        }
        assertTrue(success.size >= 184)
    }

    @Disabled("Doesn't work, result block will be Large, not Small.")
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
