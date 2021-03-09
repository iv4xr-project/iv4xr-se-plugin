package spaceEngineers.commands

import kotlin.jvm.JvmOverloads

class ObservationArgs @JvmOverloads constructor(val observationMode: Int = ObservationMode.DEFAULT.value) {
    constructor(observationMode: ObservationMode) : this(observationMode.value)
}