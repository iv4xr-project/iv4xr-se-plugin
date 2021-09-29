package spaceEngineers.controller.extensions

import spaceEngineers.controller.Definitions
import spaceEngineers.model.BlockDefinition


fun Definitions.blockDefinitionByType(blockType: String): BlockDefinition {
    return blockDefinitions().first { it.definitionId.type == blockType } ?: error("block type not found $blockType")
}
