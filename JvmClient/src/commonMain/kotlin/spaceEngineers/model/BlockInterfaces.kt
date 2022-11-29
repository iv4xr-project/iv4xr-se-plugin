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

