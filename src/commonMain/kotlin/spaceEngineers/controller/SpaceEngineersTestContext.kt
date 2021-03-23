package spaceEngineers.controller

import spaceEngineers.model.SeBlock
import spaceEngineers.model.SeObservation
import spaceEngineers.model.ToolbarLocation

data class SpaceEngineersTestContext(
    var grinderLocation: ToolbarLocation? = null,
    var torchLocation: ToolbarLocation? = null,
    var lastNewBlockId: String? = null,
    val blockTypeToToolbarLocation: MutableMap<String, ToolbarLocation> = mutableMapOf(),
    var allNewestBlocks: MutableList<SeBlock> = mutableListOf(),
    val observations: MutableList<SeObservation> = mutableListOf()
) {


    fun updateToolbarLocation(dataTable: List<Map<String, String>>) {
        blockTypeToToolbarLocation.clear()
        blockTypeToToolbarLocation.putAll(
            dataTable.map {
                it["blockType"]!! to ToolbarLocation(slot = it["slot"]!!.toInt(), page = it["page"]!!.toInt())
            }.toMap()
        )
    }

    fun blockToolbarLocation(blockType: String): ToolbarLocation {
        return blockTypeToToolbarLocation[blockType] ?: error("Toolbar location not set for type $blockType")
    }

    fun updateNewBlocks(allBlocks: List<SeBlock>) {
        allBlocks.lastOrNull()?.let {
            lastNewBlockId = it.id
        }
        allNewestBlocks.addAll(allBlocks)
    }

    fun update(seObservation: SeObservation) {
        observations.add(seObservation)
    }
}