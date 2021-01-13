package spaceEngineers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.SeAgentCommand;

public class SpaceEngEnvironmentTest {

    @Test
    public void disconnectTest() {
        var environment = SpaceEngEnvironment.localhost();

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

    @Test
    public void observeTest() {
        var environment = SpaceEngEnvironment.localhost();

        var observation = environment.getSeResponse(SeRequest.command(SeAgentCommand.observe("you")));
        Assertions.assertNotNull(observation);

        System.out.println("AgentId: " + observation.agentID);
        System.out.println("Position: " + observation.position);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

    @Test
    public void observeManyTimesTest() {
        var environment = SpaceEngEnvironment.localhost();

        for (int i = 0; i < 5; i++) {
            var observation = environment.getSeResponse(SeRequest.command(SeAgentCommand.observe("you")));
            Assertions.assertNotNull(observation);

            System.out.println("AgentId: " + observation.agentID);
            System.out.println("Position: " + observation.position);
        }

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

    @Test
    public void observeEntitiesTest() {
        var environment = SpaceEngEnvironment.localhost();

        var observation = environment.getSeResponse(SeRequest.command(SeAgentCommand.observe("you")));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.entities);
        Assertions.assertTrue(observation.entities.size() > 0);

        System.out.println("Got " + observation.entities.size() + " entities.");
        System.out.println("First entity position: " + observation.entities.get(0).position);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }


    @Test
    public void observeBlocksTest() {
        var environment = SpaceEngEnvironment.localhost();

        var observation = environment.getSeResponse(SeRequest.command(SeAgentCommand.observe("you")));
        Assertions.assertNotNull(observation);
        Assertions.assertNotNull(observation.blocks);
        Assertions.assertTrue(observation.blocks.size() > 0);

        System.out.println("Got " + observation.blocks.size() + " blocks.");
        var firstBlock = observation.blocks.get(0);
        System.out.println("First block max integrity: " + firstBlock.maxIntegrity);
        System.out.println("First block build integrity: " + firstBlock.buildIntegrity);
        System.out.println("First block integrity: " + firstBlock.integrity);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }


}
