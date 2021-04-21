package spaceEngineers.game

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcCharacterController
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.CubeSize
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3
import spaceEngineers.model.allBlocks
import testhelp.spaceEngineers
import java.lang.Thread.sleep
import kotlin.test.Test
import kotlin.test.assertEquals


fun JsonRpcCharacterController.checkBlockType(
    blockType: String,
    location: ToolbarLocation = ToolbarLocation(0, 0)
): Boolean {
    items.setToolbarItem(blockType, location)
    sleep(10)
    val toolbar = getToolbar()
    sleep(10)
    val toolbarItem = toolbar[location]
    return toolbarItem != null && toolbarItem.subType == blockType
}

fun JsonRpcCharacterController.checkPlacement(
    blockType: String,
    location: ToolbarLocation = ToolbarLocation(0, 0)
): Boolean {
    observer.observeNewBlocks()
    sleep(10)
    items.equip(location)
    sleep(10)
    items.place()
    sleep(10)
    return observer.observeNewBlocks().allBlocks.let { it.isNotEmpty() && it.first().blockType == blockType }
}


class GetToolbarTest {


    @Test
    fun getToolbar() = jsonRpcSpaceEngineers {
        val toolbar = getToolbar()
        assertEquals(toolbar[ToolbarLocation(0, 0)]?.subType, "asdf")
        assertEquals(toolbar.items.size, toolbar.pageCount * toolbar.slotCount)
    }

    @Test
    fun setToolbar() = jsonRpcSpaceEngineers {
        val blockTypes = blockDefinitions().filter { it.cubeSize == CubeSize.Large && it.size == Vec3(1, 1, 1) }
            .map { it.blockType }

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
    fun equipSmall() = spaceEngineers {
        session.loadFromTestResources("simple-place-grind-torch")
        val location = ToolbarLocation(2, 1)
        observer.observeNewBlocks()
        val blockType = "SmallHeavyBlockArmorBlock"
        items.setToolbarItem(blockType, location)
        items.equip(location)
        items.place()
        val blocks = observer.observeNewBlocks().allBlocks
        assertEquals(1, blocks.size)
        val block = blocks.first()
        assertEquals(blockType, block.blockType)
    }
}