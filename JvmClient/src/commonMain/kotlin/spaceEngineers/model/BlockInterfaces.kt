package spaceEngineers.model

// Generated file using BlockMappingGenerator.kt.
interface TerminalBlock: Block  {
    val showInInventory: Boolean
    val showInTerminal: Boolean
    val showOnHUD: Boolean
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

