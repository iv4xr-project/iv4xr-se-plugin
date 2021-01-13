package spaceEngineers.commands;

public class ObservationArgs {
    public int observationMode;

    public ObservationArgs(ObservationMode observationMode) {
        this.observationMode = observationMode.getValue();
    }

    public ObservationArgs() {
        this.observationMode = ObservationMode.DEFAULT.getValue();
    }
}
