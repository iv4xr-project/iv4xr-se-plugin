package spaceEngineers.controller

import spaceEngineers.model.BlockId
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I

interface BlocksAdmin {

    fun placeAt(
        blockDefinitionId: DefinitionId,
        position: Vec3F,
        orientationForward: Vec3F,
        orientationUp: Vec3F,
        color: Vec3F? = null,
    ): String

    fun placeInGrid(
        blockDefinitionId: DefinitionId,
        gridId: String,
        minPosition: Vec3I,
        orientationForward: Vec3I,
        orientationUp: Vec3I,
        color: Vec3F? = null,
    ): String

    fun remove(blockId: BlockId)
    fun setIntegrity(blockId: BlockId, integrity: Float)
    fun setCustomName(blockId: BlockId, customName: String)
    fun createOrUpdateGroup(name: String, gridId: String, blockIds: List<BlockId>)
    fun mapButtonToBlock(buttonBlockId: BlockId, buttonIndex: Int, action: String, targetId: BlockId)
    fun mapButtonToGroup(buttonBlockId: BlockId, buttonIndex: Int, action: String, groupName: String)
    val warhead: WarheadAdmin
}

interface WarheadAdmin {
    fun explode(blockId: BlockId)
    fun detonate(blockId: BlockId)
    fun startCountDown(blockId: BlockId): Boolean
    fun stopCountdown(): Boolean
    fun setArmed(blockId: BlockId, armed: Boolean)
}
