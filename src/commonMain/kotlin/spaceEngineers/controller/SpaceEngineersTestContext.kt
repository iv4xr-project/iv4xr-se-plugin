package spaceEngineers.controller

import spaceEngineers.model.*

data class SpaceEngineersTestContext(
    var grinderLocation: ToolbarLocation? = null,
    var torchLocation: ToolbarLocation? = null,
    var lastNewBlockId: String? = null,
    val blockTypeToToolbarLocation: MutableMap<String, ToolbarLocation> = mutableMapOf(),
    var allNewestBlocks: MutableList<SeBlock> = mutableListOf(),
    val observationHistory: MutableList<SeObservation> = mutableListOf(),
    var platformOrientationUp: Vec3? = null,
) {

    fun updatePlatformOrientationUpIfNotSet(seObservation: SeObservation) {
        if (platformOrientationUp == null) {
            updatePlatformOrientationUp(seObservation)
        }
    }

    fun updatePlatformOrientationUp(seObservation: SeObservation) {
        val characterOrientationUp = seObservation.orientationUp
        val blocks = seObservation.allBlocks
        platformOrientationUp = blocks.map { it.orientationUp }
            .maxByOrNull { it!!.distanceTo(characterOrientationUp) }
    }

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

    fun addToHistory(seObservation: SeObservation) {
        observationHistory.add(seObservation)
    }
}