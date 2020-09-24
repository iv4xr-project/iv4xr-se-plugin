package spaceEngineers;

import environments.SeSocketEnvironment;

public class SpaceEngEnvironment extends SeSocketEnvironment {

    public SpaceEngEnvironment(String host, int port) {
        super(host, port);
    }

    public static SpaceEngEnvironment localhost() {
        return new SpaceEngEnvironment("localhost", DEFAULT_PORT);
    }
}
