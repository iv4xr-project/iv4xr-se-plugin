package spaceEngineers.model

import spaceEngineers.model.BootsState.Companion.DISABLED
import spaceEngineers.model.BootsState.Companion.ENABLED
import spaceEngineers.model.BootsState.Companion.INIT
import spaceEngineers.model.BootsState.Companion.PROXIMITY

enum class BootsColour(val values: Set<BootsState>) {
    WHITE(setOf(INIT, DISABLED)), YELLOW(setOf(PROXIMITY)), GREEN(setOf(ENABLED));

    val uIntValues: List<UInt>
        get() = values.map { it.value }
}
