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
                SeAgentCommand.observe("you", new ObservationArgs(ObservationMode.NEW_BLOCKS))));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.blocks);
        System.out.println("Got " + observation.blocks.size() + " blocks.");

        observation = environment.getSeResponse(SeRequest.command(
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
        for (var block : observation.blocks.subList(0, Math.min(2, observation.blocks.size()))) {
            System.out.println("Block max integrity: " + block.maxIntegrity);
            System.out.println("Block build integrity: " + block.buildIntegrity);
            System.out.println("Block integrity: " + block.integrity);
            System.out.println("Block type: " + block.blockType);
            System.out.println("Block position min: " + block.minPosition);
            System.out.println("Block position max: " + block.maxPosition);
            System.out.println("Block size        : " + block.size);
            System.out.println("Block orientation fwd: " + block.orientationForward);
            System.out.println("Block orientation up : " + block.orientationUp);
        }

        Assertions.assertEquals(1, observation.blocks.size(), "There should be exactly 1 new block.");

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

}
