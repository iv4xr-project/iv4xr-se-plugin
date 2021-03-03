package spaceEngineers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.*;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class InteractionTest {

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
    public void equipToolbarItemTest() {
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you",
                        new InteractionArgs(InteractionType.EQUIP, 4, -1, true))));
        Assertions.assertNotNull(observation);
    }

    @Test
    public void pageAndEquipTest() {
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.EQUIP, 4, 2))));
        Assertions.assertNotNull(observation);
    }

    @Test
    public void equipAndPlace() {
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.EQUIP,3))));
        Assertions.assertNotNull(observation);

        observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.PLACE))));
        Assertions.assertNotNull(observation);
    }

    @Test
    public void checkNewBlock() {
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.observe("you", new ObservationArgs(ObservationMode.NEW_BLOCKS))));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.grids);
        System.out.println("Got " + observation.grids.size() + " grids.");

        for (var grid : observation.grids) {
            System.out.println("Got " + grid.blocks.size() + " blocks (grid id: " + grid.id + ").");
            System.out.println("Grid position: " + grid.position);
        }

        observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.EQUIP,3, 0))));
        Assertions.assertNotNull(observation);

        observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", new InteractionArgs(InteractionType.PLACE))));
        Assertions.assertNotNull(observation);

        observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.observe("you", new ObservationArgs(ObservationMode.NEW_BLOCKS))));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.grids);
        Assertions.assertTrue(observation.grids.size() > 0, "Expecting non-zero grid count.");

        List<SeBlock> blocks = null;
        for (var grid : observation.grids) {
            blocks = grid.blocks;
            System.out.println("Got " + blocks.size() + " blocks (grid id: " + grid.id + ").");

            if (blocks.size() > 0)  // Take first nonempty grid.
                break;
        }

        for (var block : blocks.subList(0, Math.min(2, blocks.size()))) {
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

        Assertions.assertEquals(1, blocks.size(), "There should be exactly 1 new block.");
    }

}
