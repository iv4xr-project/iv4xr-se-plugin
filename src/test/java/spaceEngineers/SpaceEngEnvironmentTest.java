package spaceEngineers;

import nl.uu.cs.aplib.mainConcepts.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpaceEngEnvironmentTest {

    @Test
    public void disconnectTest() {
        var environment = new SpaceEngEnvironment("localhost", 9678);

        var reply = environment.sendCommand(
                new Environment.EnvOperation("me", "you", "request", "hello!"));

        System.out.println("Reply: " + (String)reply);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }
}
