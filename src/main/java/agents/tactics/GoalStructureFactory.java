/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.tactics;

import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.Goal;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import world.BeliefState;
import world.Entity;

import java.util.function.Predicate;

import static nl.uu.cs.aplib.AplibEDSL.*;

import eu.iv4xr.framework.mainConcepts.ObservationEvent.VerdictEvent;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import static eu.iv4xr.framework.Iv4xrEDSL.* ;

/**
 * This class will serve as a factory capable of generating goal structures which can be used by the agent
 */
public class GoalStructureFactory {
    /**
     * This method will construct a goal structure in which the agent will move to a known position
     *
     * @param goalPosition: Position to where the agent should move
     * @return The goal structure with a default move tactic which will move the agent to the goal position
     */
    public static Goal positionReached(Vec3 goalPosition) {
        //define the goal
        Goal goal = new Goal("Reach position " + goalPosition.toString()).toSolve((BeliefState belief) -> {
            //check if the agent is close to the goal position
            return belief.withinRange(goalPosition);
            //if (belief.position == null) return false;
            //return goalPosition.distance(belief.position) < 0.4;
        });

        //define the goal structure
        Goal g = goal.withTactic(FIRSTof(//the tactic used to solve the goal
                TacticsFactory.navigateTo(goalPosition),//move to the goal position
                TacticsFactory.explore(),//explore if the goal position is unknown
                TacticsFactory.observe()));//find the agent's own position
        return g;
    }

    /**
     * This method will return a goal structure in which the agent will sequentially move along the goal positions
     *
     * @param goalPositions: A list of coordinates to which the agent wants to move
     * @return A goal structure in which the agent will try to move sequentially along the goal positions
     */
    public static GoalStructure positionsVisited(Vec3... goalPositions) {
        GoalStructure[] subGoals = new GoalStructure[goalPositions.length];

        for (int i = 0; i < goalPositions.length; i++) {
            subGoals[i] = positionReached(goalPositions[i]).lift();
        }
        return SEQ(subGoals);
    }

    /**
     * This method will construct a goal structure in which the agent will move to the 
     * in-game entity with the given id
     *
     * @param entityId: The id of the entity to which the agent wants to move to
     * @return The goal structure with a default move tactic which will move the agent to the object
     */
    public static Goal entityReached(String entityId) {
        Goal goal = new Goal(String.format("Reach object [%s]",entityId)).toSolve((BeliefState belief) -> belief.withinRange(entityId));

        //define the goal structure
        Goal g = goal.withTactic(FIRSTof( //the tactic used to solve the goal
                TacticsFactory.navigateTo(entityId),//try to move to the goal object
                TacticsFactory.explore(),//find the goal object
                TacticsFactory.observe()));//find the agent's own position
        return g;
    }

    /**
     * This method will return a goal structure in which the agent will sequentially move along 
     * the given entities (specified through their ids).
     *
     * @param entitiesIds: A list of object ids to which the agent wants to move
     * @return A goal structure in which the agent will try to walk along the goal id's
     */
    public static GoalStructure entitiesVisited(String... entitiesIds) {
        GoalStructure[] subGoals = new GoalStructure[entitiesIds.length];

        for (int i = 0; i < entitiesIds.length; i++) {
            subGoals[i] = entityReached(entitiesIds[i]).lift();
        }
        return SEQ(subGoals);
    }

    /**
     * Combine movement with interaction to check if an entity is activated
     *
     * @param entityId The entity to walk to and interact with
     * @return A goal structure
     * @Incomplete: this goal should check if the object has a given desired state, and perhaps include a position check in the goal predicate itself
     */
    public static GoalStructure entityReachedAndInteracted(String entityId) {
        //move to the object
        Goal goal1 = goal(String.format("Reach [%s]", entityId)).toSolve((BeliefState belief) ->
                belief.position != null && belief.canInteract(entityId));

        //interact with the object
        Goal goal2 = goal(String.format("Interact with [%s]", entityId)).toSolve((BeliefState belief) -> true);

        //Set the tactics with which the goals will be solved
        GoalStructure g1 = goal1.withTactic(FIRSTof( //the tactic used to solve the goal
                TacticsFactory.navigateTo(entityId), //try to move to the object
                TacticsFactory.explore(), //find the object
                TacticsFactory.observe(),
                ABORT())) //find the agent's own position
                .lift();
        GoalStructure g2 = goal2.withTactic(FIRSTof( //the tactic used to solve the goal
                TacticsFactory.interact(entityId),// interact with the object
                TacticsFactory.observe(),
                ABORT())) // observe the objects
                .lift();

        return SEQ(g1, g2);
    }

    /**
     * Interact with a variable number of buttons sequentially before moving to the goal
     *
     * @param goal    The name of the object of the final goal
     * @param buttons A collection of objects to interact with
     * @return A sequence of goals moving to and interacting with the given objects
     */
    public static GoalStructure buttonsVisited_thenGoalVisited(String goal, String... buttons) {
        var gs = new GoalStructure[buttons.length + 1];
        for (int i = 0; i < buttons.length; i++) gs[i] = entityReachedAndInteracted(buttons[i]);
        gs[buttons.length] = entityReached(goal).lift();

        return SEQ(gs);
    }

    /**
     * This goal will make sure that the agent has the latest observation of the entity
     * Getting the entity within sight is enough to complete this goal.
     * @param id: entityId
     * @return Goal
     */
    public static Goal entityInspected(String id){
        return goal("Inspect " + id)
                .toSolve((BeliefState b) -> b.evaluateEntity(id, e -> e.lastUpdated == b.lastUpdated))
                .withTactic(FIRSTof(
                        TacticsFactory.navigateTo(id),
                        TacticsFactory.explore(),
                        TacticsFactory.observe(),
                        ABORT()));
    }

    /**
     * Extention of inspect
     * Getting the entity within sight is enough to complete this goal.
     * @param id: entityId
     * @return Goal
     */
    public static GoalStructure entityInspected(String id, Predicate<Entity> predicate){
        return SEQ(
            entityInspected(id).lift(),
            goal("Evaluate " + id)
            .toSolve((BeliefState b) -> b.evaluateEntity(id, predicate))
            .withTactic(FIRSTof(
                    TacticsFactory.observeOnce(),
                    ABORT())).lift()
        );
    }
    
    /**
     * Create a test-goal to check the state of an in-game entity, whether it satisfies the given predicate.
     * Internally, this goal will first spend one tick to get a fresh observation, then at the next tick it
     * will do the checking.
     * 
     * @param agent  The test agent to do the checking.
     * @param id     The id of the in-game entity to check.
     * @param info   Some string describing the check.
     * @param predicate  The predicate that is expected to hold on the entity.
     * @return
     */
    public static GoalStructure entityInvariantChecked(TestAgent agent, String id, String info, Predicate<Entity> predicate){
        return SEQ(
            entityInspected(id).lift(),
            testgoal("Evaluate " + id, agent)
            .toSolve((BeliefState b) -> true) // nothing to solve
            .invariant(agent,                 // something to check :)
            		(BeliefState b) -> {
            			if (b.evaluateEntity(id, predicate))
            			   return new VerdictEvent("Object-check " + id, info, true) ;
            			else 
            			   return new VerdictEvent("Object-check " + id, info, false) ;
            			
            		}
            		)
            .withTactic(FIRSTof(
                    TacticsFactory.observeOnce(),
                    ABORT())).lift()
        );
    }

    /**
     * This goal structure will cause the agent to share its memory once with all connected agents in the broadcast
     *
     * @param id: The id of the sending agent
     * @return A goal structure which will be concluded when the agent shared its memory once
     */
    public static GoalStructure memorySent(String id){
        return goal("Share memory").toSolve((BeliefState belief) -> true).withTactic(
                TacticsFactory.shareMemory(id)
        ).lift();
    }

    /**
     * This goal structure will cause the agent to send a ping to the target agent
     * @param idFrom: The id of the sending agent
     * @param idTo: The id of the receiving agent
     * @return A goal structure which will be concluded when the agent send a ping to the target agent
     */
    public static Goal pingSent(String idFrom, String idTo){
        return new Goal("Send ping").toSolve((BeliefState belief) -> true).withTactic(
                TacticsFactory.sendPing(idFrom, idTo)
        );
    }
}
