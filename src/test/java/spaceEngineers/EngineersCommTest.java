package spaceEngineers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import spaceEngineers.environments.SeSocketEnvironment;
import nl.uu.cs.aplib.mainConcepts.Environment;
import org.junit.jupiter.api.Test;

public class EngineersCommTest {

    SpaceEngEnvironment environment;

    @BeforeEach
    public void beforeEach() {
        environment = SpaceEngEnvironment.localhost();
    }

    @AfterEach
    public void afterEach() {
        environment.close();
        environment = null;
    }

    @Test
    public void commScrewingTest() {
        // These messages are invalid, so we basically test error handling here.
        // (The server cuts of the connection after the first message.)
        var messages = new String[] { "hello", "you", "fool" };

        for (var message : messages) {
            var reply = environment.sendCommand(
                    new Environment.EnvOperation("me", "you", "request", message, Object.class));

            System.out.println("Reply: " + (String)reply);
        }
    }

}
