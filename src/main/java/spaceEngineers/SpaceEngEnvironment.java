package spaceEngineers;

import spaceEngineers.environments.SeSocketEnvironment;

// TODO(GAI/PP): Redesign this by extending W3DEnvironment (@see class LabRecruitsEnvironment for inspiration)

public class SpaceEngEnvironment extends SeSocketEnvironment {

    public SpaceEngEnvironment(String host, int port) {
        super(host, port);
    }

    public static SpaceEngEnvironment localhost() {
        return new SpaceEngEnvironment("localhost", DEFAULT_PORT);
    }
}
