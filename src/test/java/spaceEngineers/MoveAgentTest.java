package spaceEngineers;

import communication.agent.AgentCommand;
import helperclasses.datastructures.Vec3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spaceEngineers.commands.MovementArgs;
import spaceEngineers.commands.SeAgentCommand;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class MoveAgentTest {

    @Test
    public void moveTest() {
        var environment = SpaceEngEnvironment.localhost();

        // Single move request is not actually visible on the character movement, see manyMovesTest below
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.moveTowardCommand("you", new Vec3(0,0,-1.0), false)));
        Assertions.assertNotNull(observation);

        System.out.println("AgentId: " + observation.agentID);
        System.out.println("Position: " + observation.position);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

    @Test
    public void moveAndRotateTest() {
        var environment = SpaceEngEnvironment.localhost();

        // Single move request is not actually visible on the character movement, see manyMovesTest below
        var observation = environment.getSeResponse(SeRequest.command(
                SeAgentCommand.moveAndRotate("you", new Vec3(0,0,-1.), new Vec3(0.3, 0, 0), 0)));
        Assertions.assertNotNull(observation);

        System.out.println("AgentId: " + observation.agentID);
        System.out.println("Position: " + observation.position);

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

    @Test
    public void manyMovesTest() {

        var environment = SpaceEngEnvironment.localhost();

        var moves = new ArrayList<Vec3>();
        BiConsumer<Vec3, Integer> addMoves = (move, count) -> {
            for (int n = 0; n < count; n++)
                moves.add(move);
        };

        addMoves.accept(new Vec3(0, 0, -1), 40);  // Forward
        addMoves.accept(new Vec3(1, 0, 0), 10);   // Right
        addMoves.accept(new Vec3(0, 0, -1), 20);
        addMoves.accept(new Vec3(-1, 0, 0), 20);  // Left

        moves.add(new Vec3(0, 0.5, 0)); // Up

        for (var move : moves) {

            var observation = environment.getSeResponse(SeRequest.command(
                    SeAgentCommand.moveTowardCommand("you", move, false)));
            Assertions.assertNotNull(observation);

            System.out.println("Position: " + observation.position);
        }

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }

    @Test
    public void manyMovesAndRotationsTest() {

        var environment = SpaceEngEnvironment.localhost();

        var moves = new ArrayList<MovementArgs>();
        BiConsumer<MovementArgs, Integer> addMoves = (movementArgs, count) -> {
            for (int n = 0; n < count; n++)
                moves.add(movementArgs);
        };

        var forwardArgs = new MovementArgs(new Vec3(0, 0, -1));
        addMoves.accept(forwardArgs, 30);

        var rotateArgs = new MovementArgs(Vec3.zero(), new Vec3(0, 9.0, 0), 0);
        addMoves.accept(rotateArgs, 20);

        addMoves.accept(forwardArgs, 30);
        addMoves.accept(new MovementArgs(new Vec3(-1, 0, 0)), 20);  // Left

        var rollArgs = new MovementArgs(Vec3.zero(), Vec3.zero(), -2);
        addMoves.accept(rollArgs, 15);

        var allArgs = new MovementArgs(new Vec3(0, 0.7, 0.2), new Vec3(5, 7, 0), 1.5f);
        addMoves.accept(allArgs, 40);

        for (var move : moves) {

            var observation = environment.getSeResponse(SeRequest.command(
                    SeAgentCommand.moveAndRotate("you", move)));
            Assertions.assertNotNull(observation);

            System.out.println("Position: " + observation.position);
        }

        boolean result = environment.getSeResponse(SeRequest.disconnect());
        Assertions.assertTrue(result);
    }
}
