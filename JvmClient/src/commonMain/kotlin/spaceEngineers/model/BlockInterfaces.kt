package spaceEngineers.model

// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.

interface TerminalBlock: Block  {
    val showInInventory: Boolean
    val showInTerminal: Boolean
    val showOnHUD: Boolean
    val customName: String
    val customData: String
}

interface FunctionalBlock: TerminalBlock  {
    val enabled: Boolean
}

interface DoorBase: FunctionalBlock  {
    val open: Boolean
    val anyoneCanUse: Boolean
}

interface FueledPowerProducer: FunctionalBlock  {
    val maxOutput: Float
    val currentOutput: Float
    val capacity: Float
}

interface Warhead: TerminalBlock  {
    val isCountingDown: Boolean
    val isArmed: Boolean
}

interface MedicalRoom: FunctionalBlock  {
    val suitChangeAllowed: Boolean
    val customWardrobesEnabled: Boolean
    val spawnName: String
    val respawnAllowed: Boolean
    val refuelAllowed: Boolean
    val healingAllowed: Boolean
    val spawnWithoutOxygenEnabled: Boolean
    val forceSuitChangeOnRespawn: Boolean
}

interface TimerBlock: FunctionalBlock  {
    val silent: Boolean
    val triggerDelay: Float
    val toolbar: Toolbar
}

interface SensorBlock: FunctionalBlock  {
    val isActive: Boolean
    val fieldMin: Vec3F
    val fieldMax: Vec3F
    val maxRange: Float
    val filters: Int
    val toolbar: Toolbar
}

interface GravityGenerator: GravityGeneratorBase  {
    val fieldSize: Vec3F
}

interface GravityGeneratorSphere: GravityGeneratorBase  {
    val radius: Float
}

interface GravityGeneratorBase: FunctionalBlock  {
    val gravityAcceleration: Float
}

