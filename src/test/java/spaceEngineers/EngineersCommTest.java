package spaceEngineers;

import environments.SeSocketEnvironment;
import nl.uu.cs.aplib.mainConcepts.Environment;
import org.junit.jupiter.api.Test;

public class EngineersCommTest {

    @Test
    public void commTest() {

        var environment = new SeSocketEnvironment("localhost", 9678);

        var messages = new String[] { "hello", "you", "fool" };

        for (var message : messages) {
            var reply = environment.sendCommand(
                    new Environment.EnvOperation("me", "you", "request", message));

            System.out.println("Reply: " + (String)reply);
        }
    }
}
