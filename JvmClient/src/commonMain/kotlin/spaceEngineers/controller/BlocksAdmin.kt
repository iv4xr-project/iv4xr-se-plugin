package spaceEngineers.controller

import spaceEngineers.model.BlockId
import spaceEngineers.model.CubeGrid
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I

interface BlocksAdmin {

    fun placeAt(
        blockDefinitionId: DefinitionId,
        position: Vec3F,
        orientationForward: Vec3F = Vec3F.FORWARD,
        orientationUp: Vec3F = Vec3F.UP,
        color: Vec3F? = null,
    ): CubeGrid

    fun placeInGrid(
        blockDefinitionId: DefinitionId,
        gridId: String,
        minPosition: Vec3I,
        orientationForward: Vec3I = Vec3I.FORWARD,
        orientationUp: Vec3I = Vec3I.UP,
        color: Vec3F? = null,
    ): String

    fun remove(blockId: BlockId)
    fun setIntegrity(blockId: BlockId, integrity: Float)
    fun setCustomName(blockId: BlockId, customName: String)
    fun createOrUpdateGroup(name: String, gridId: String, blockIds: List<BlockId>)
    fun mapButtonToBlock(buttonBlockId: BlockId, buttonIndex: Int, action: String, targetId: BlockId)
    fun mapButtonToGroup(buttonBlockId: BlockId, buttonIndex: Int, action: String, groupName: String)
    val warhead: WarheadAdmin
    val medicalRoom: MedicalRoomAdmin
    val functionalBlock: FunctionalBlockAdmin
    val terminalBlock: TerminalBlockAdmin
    val sensorBlock: SensorBlockAdmin
}

interface SensorBlockAdmin {
    fun setFieldMin(blockId: String, fieldMin: Vec3F)
    fun setFieldMax(blockId: String, fieldMax: Vec3F)
}

interface MedicalRoomAdmin {
    fun setRespawnAllowed(blockId: String, respawnAllowed: Boolean)
    fun setHealingAllowed(blockId: String, healingAllowed: Boolean)
    fun setRefuelAllowed(blockId: String, refuelAllowed: Boolean)
    fun setSpawnWithoutOxygenEnabled(blockId: String, spawnWithoutOxygenEnabled: Boolean)
}

interface WarheadAdmin {
    fun explode(blockId: BlockId)
    fun detonate(blockId: BlockId)
    fun startCountDown(blockId: BlockId): Boolean
    fun stopCountdown(): Boolean
    fun setArmed(blockId: BlockId, armed: Boolean)
}

interface FunctionalBlockAdmin {
    fun setEnabled(blockId: BlockId, enabled: Boolean)
}

interface TerminalBlockAdmin {
    fun setCustomName(blockId: BlockId, customName: String)
    fun setCustomData(blockId: BlockId, customData: String)
    fun setShowInInventory(blockId: String, showInInventory: Boolean)
    fun setShowInTerminal(blockId: String, showInTerminal: Boolean)
    fun setShowOnHUD(blockId: String, showOnHUD: Boolean)
}
