package spaceEngineers.commands;

public class SeSessionCommand {
    // TODO(PP): Add request type enum. So far only load command is supported.
    public String scenarioPath;

    private SeSessionCommand(String scenarioPath) {

        this.scenarioPath = scenarioPath;
    }

    public static SeSessionCommand load(String scenarioPath) {
        return new SeSessionCommand(scenarioPath);
    }
}
