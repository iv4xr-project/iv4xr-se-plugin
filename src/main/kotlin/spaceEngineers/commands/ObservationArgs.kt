package spaceEngineers.commands

class ObservationArgs @JvmOverloads constructor(val observationMode: Int = ObservationMode.DEFAULT.value) {
    constructor(observationMode: ObservationMode) : this(observationMode.value)
}