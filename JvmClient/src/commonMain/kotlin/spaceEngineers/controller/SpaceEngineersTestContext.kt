package spaceEngineers.controller

import spaceEngineers.model.*
import spaceEngineers.model.extensions.allBlocks

data class SpaceEngineersTestContext(
    var grinderLocation: ToolbarLocation? = null,
    var torchLocation: ToolbarLocation? = null,
    var lastNewBlock: Block? = null,
    val blockTypeToToolbarLocation: MutableMap<String, ToolbarLocation> = mutableMapOf(),
    var allNewestBlocks: MutableList<Block> = mutableListOf(),
    val observationHistory: MutableList<Observation> = mutableListOf(),
    var platformOrientationUp: Vec3? = null,
) {

    val lastNewBlockId: String?
        get() = lastNewBlock?.id

    fun updatePlatformOrientationUpIfNotSet(observation: Observation) {
        if (platformOrientationUp == null) {
            updatePlatformOrientationUp(observation)
        }
    }

    fun updatePlatformOrientationUp(observation: Observation) {
        val characterOrientationUp = observation.orientationUp
        val blocks = observation.allBlocks
        platformOrientationUp = blocks.map { it.orientationUp }
            .maxByOrNull { it.distanceTo(characterOrientationUp) }
    }

    fun updateToolbarLocation(dataTable: List<Map<String, String>>) {
        blockTypeToToolbarLocation.clear()
        blockTypeToToolbarLocation.putAll(
            dataTable.associate {
                it["blockType"]!! to ToolbarLocation(slot = it["slot"]!!.toInt(), page = it["page"]!!.toInt())
            }
        )
    }

    fun blockToolbarLocation(blockType: String): ToolbarLocation {
        return blockTypeToToolbarLocation[blockType] ?: error("Toolbar location not set for type $blockType")
    }

    fun updateNewBlocks(allBlocks: List<Block>) {
        allBlocks.lastOrNull()?.let {
            lastNewBlock = it
        }
        allNewestBlocks.addAll(allBlocks)
    }

    fun addToHistory(observation: Observation) {
        observationHistory.add(observation)
    }
}
