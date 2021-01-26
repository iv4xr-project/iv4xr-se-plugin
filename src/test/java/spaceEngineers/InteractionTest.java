package spaceEngineers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.*;

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
    public void pageAndEquipTest() {
        var environment = SpaceEngEnvironment.localhost();

        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.EQUIP, 4, 2))));
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

    @Test
    public void checkNewBlock() {
        var environment = SpaceEngEnvironment.localhost();

        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.EQUIP,3))));
        Assertions.assertNotNull(observation);

        observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.PLACE))));
        Assertions.assertNotNull(observation);

        observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.observe("you", new ObservationArgs(ObservationMode.NEW_BLOCKS))));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.blocks);
        Assertions.assertTrue(observation.blocks.size() > 0, "Expecting non-zero block count.");

        System.out.println("Got " + observation.blocks.size() + " blocks.");
        var firstBlock = observation.blocks.get(0);
        System.out.println("First block max integrity: " + firstBlock.maxIntegrity);
        System.out.println("First block build integrity: " + firstBlock.buildIntegrity);
        System.out.println("First block integrity: " + firstBlock.integrity);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

}
