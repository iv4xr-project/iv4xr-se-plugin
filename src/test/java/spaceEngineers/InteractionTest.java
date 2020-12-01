package spaceEngineers;

import helperclasses.datastructures.Vec3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.InteractionArgs;
import spaceEngineers.commands.SeAgentCommand;

public class InteractionTest {

    @Test
    public void equipToolbarItemTest() {
        var environment = SpaceEngEnvironment.localhost();

        // Single move request is not actually visible on the character movement, see manyMovesTest below
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", 5)));
        Assertions.assertNotNull(observation);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }
}
