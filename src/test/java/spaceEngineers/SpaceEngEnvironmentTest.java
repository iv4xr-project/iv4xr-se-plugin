package spaceEngineers;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.ObservationArgs;
import spaceEngineers.commands.ObservationMode;
import spaceEngineers.commands.SeAgentCommand;

public class SpaceEngEnvironmentTest {

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
    public void observeTest() {
        var observation = environment.getSeResponse(SeRequest.command(SeAgentCommand.observe("you")));
        Assertions.assertNotNull(observation);

        System.out.println("AgentId: " + observation.agentID);
        System.out.println("Position: " + observation.position);
        System.out.println("OrientationFwd: " + observation.orientationForward);
        System.out.println("OrientationUp : " + observation.orientationUp);
    }

    @Test
    public void observeManyTimesTest() {
        for (int i = 0; i < 5; i++) {
            var observation = environment.getSeResponse(SeRequest.command(SeAgentCommand.observe("you")));
            Assertions.assertNotNull(observation);

            System.out.println("AgentId: " + observation.agentID);
            System.out.println("Position: " + observation.position);
        }
    }

    @Test
    public void observeEntitiesTest() {
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.observe("you", new ObservationArgs(ObservationMode.ENTITIES))));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.entities);
        Assertions.assertTrue(observation.entities.size() > 0);

        System.out.println("Got " + observation.entities.size() + " entities.");
        System.out.println("First entity position: " + observation.entities.get(0).position);
    }


    @Test
    public void observeBlocksTest() {
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.observe("you", new ObservationArgs(ObservationMode.BLOCKS))));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.grids);
        Assertions.assertEquals(1, observation.grids.size(), "Expecting 1 grid count.");
        Assertions.assertEquals(1, observation.grids.get(0).blocks.size(), "Expecting 1 grid block count.");

        System.out.println("Got " + observation.grids.size() + " grids.");
        var firstBlock = observation.grids.get(0).blocks.get(0);
        System.out.println("First block max integrity: " + firstBlock.maxIntegrity);
        System.out.println("First block build integrity: " + firstBlock.buildIntegrity);
        System.out.println("First block integrity: " + firstBlock.integrity);
        System.out.println("First block type: " + firstBlock.blockType);

    }


}
