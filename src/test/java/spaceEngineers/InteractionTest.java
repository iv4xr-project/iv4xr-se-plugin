package spaceEngineers;

import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.InteractionArgs;
import spaceEngineers.commands.InteractionType;
import spaceEngineers.commands.SeAgentCommand;

public class InteractionTest {

    @Test
    public void equipToolbarItemTest() {
        var environment = SpaceEngEnvironment.localhost();

        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.EQUIP, 4))));
        Assertions.assertNotNull(observation);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

    @Test
    public void equipAndPlace() {
        var environment = SpaceEngEnvironment.localhost();

        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.EQUIP,3))));
        Assertions.assertNotNull(observation);

        observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.PLACE))));
        Assertions.assertNotNull(observation);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

}
