package spaceEngineers.game

import org.junit.jupiter.api.Disabled
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.game.mockable.filterForScreenshots
import spaceEngineers.model.DataToolbarItemDefinition
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.extensions.allBlocks
import testhelp.MockOrRealGameTest
import testhelp.assertGreaterThan
import kotlin.test.Test
import kotlin.test.assertEquals

class GetToolbarTest : MockOrRealGameTest() {

    @Test
    fun toolbar() = testContext {
        val toolbar = items.toolbar()
        assertEquals((toolbar[ToolbarLocation(0, 0)] as DataToolbarItemDefinition?)?.id?.type, "LargeBlockArmorBlock")
        assertEquals(toolbar.items.size, toolbar.pageCount * toolbar.slotCount)
    }

    @Test
    fun setToolbar() = testContext {
        val blockTypes = definitions.blockDefinitions().filterForScreenshots().map { it.definitionId }

        val location = ToolbarLocation(0, 2)
        val (success, fail) = blockTypes.partition { definitionId ->
            delay(10)
            (checkBlockType(definitionId, location) && checkPlacement(definitionId, location))
        }
        assertGreaterThan(success.size, 184)
    }

    @Disabled("Doesn't work, result block will be Large, not Small.")
    @Test
    fun equipSmall() = testContext {
        val location = ToolbarLocation(2, 1)
        observer.observeNewBlocks()
        val blockDefinition = DefinitionId.cubeBlock("SmallHeavyBlockArmorBlock")
        items.setToolbarItem(blockDefinition, location)
        items.equip(location)
        blocks.place()
        val blocks = observer.observeNewBlocks().allBlocks
        assertEquals(1, blocks.size)
        val block = blocks.first()
        assertEquals(blockDefinition.type, block.definitionId.type)
    }

    suspend fun SpaceEngineers.checkBlockType(
        definitionId: DefinitionId,
        location: ToolbarLocation = ToolbarLocation(0, 0)
    ): Boolean {
        items.setToolbarItem(definitionId, location)
        delay(10)
        val toolbar = items.toolbar()
        delay(10)
        val toolbarItem = toolbar[location] as DataToolbarItemDefinition?
        return toolbarItem != null && toolbarItem.id.type == definitionId.type
    }

    suspend fun SpaceEngineers.checkPlacement(
        definitionId: DefinitionId,
        location: ToolbarLocation = ToolbarLocation(0, 0)
    ): Boolean {
        observer.observeNewBlocks()
        delay(10)
        items.equip(location)
        delay(10)
        blocks.place()
        delay(10)
        return (observer.observeNewBlocks().allBlocks.let { it.isNotEmpty() && it.first().definitionId == definitionId }).also {
            observer.observeBlocks().allBlocks.forEach {
                admin.blocks.remove(it.id)
            }
        }
    }
}
