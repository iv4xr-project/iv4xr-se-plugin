package spaceEngineers;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.SeSessionCommand;

@Ignore("Not ready for unit testing")
public class SessionRequestTest {

    // Ignore annotation somehow not working in IDEA
    //@Test
    public void sessionLoadTest() {
        var environment = SpaceEngEnvironment.localhost();

        // TODO(PP): support relative path
        boolean result = environment.getSeResponse(SeRequest.session(SeSessionCommand.load(
                "C:\\Users\\<user>\\AppData\\Roaming\\SpaceEngineers\\Saves\\76561198248453892\\Alien Planet 2020-09-18 12-41")));
        Assertions.assertTrue(result);

        boolean disconnected = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(disconnected);
    }

}
