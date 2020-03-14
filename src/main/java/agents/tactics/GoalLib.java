/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.tactics;

import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.Goal;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import world.BeliefState;
import world.Entity;

import java.util.function.Predicate;

import static nl.uu.cs.aplib.AplibEDSL.*;

import eu.iv4xr.framework.mainConcepts.ObservationEvent.VerdictEvent;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import static eu.iv4xr.framework.Iv4xrEDSL.* ;

/**
 * This class provide a set of standard useful sub-goals/sub-goal-structures
 * to be used by test agents to test Lab Recruits.
 */
public class GoalLib {
	
	
	public static Goal justObserve() {
		Goal g = new Goal("Just making an observation") 
				. toSolve(b -> true) 
				.withTactic(TacticLib.observe()) ;
		return g ;
	}
	
    /**
     * This method will construct a goal in which the agent will move to a known position.
     * The goal is solved if the position is "in-range" from the agent.
     * It will fail the goal if the agent no longer believes the position to be reachable.
     */
    public static Goal positionIsInRange(Vec3 goalPosition) {
        //define the goal
        Goal goal = new Goal("This position is in-range: " + goalPosition.toString()).toSolve((BeliefState belief) -> {
            //check if the agent is close to the goal position
            return belief.withinRange(goalPosition);
            //if (belief.position == null) return false;
            //return goalPosition.distance(belief.position) < 0.4;
        });

        //define the goal structure
        Goal g = goal.withTactic(FIRSTof(//the tactic used to solve the goal
                TacticLib.navigateTo(goalPosition),//move to the goal position
                TacticLib.explore(), //explore if the goal position is unknown
                ABORT())) ;
        return g;
    }

    /**
     * This method will return a goal structure in which the agent will sequentially move along 
     * specified positions. The agent does not literally have to be at eact of these position;
     * being 'in-range' of each of them is enough.
     * 
     * The goal-structure will fail if one of its position fails, which happen if the agent no
     * longer believs that the position is reachable.
     */
    public static GoalStructure positionsVisited(Vec3... positions) {
        GoalStructure[] subGoals = new GoalStructure[positions.length];

        for (int i = 0; i < positions.length; i++) {
            subGoals[i] = positionIsInRange(positions[i]).lift();
        }
        return SEQ(subGoals);
    }

    /**
     * This method will construct a goal structure in which the agent will move to the 
     * in-game entity with the given id. Moving to a close enough distance is enough to solve
     * this goal. 
     * 
     * Note: this goal should in principle also work on a moving entity.
     * 
     * The goal fails if the agent no longer believes that the entity is reachable.
     */
    public static Goal entityIsInRange(String entityId) {
        Goal goal = new Goal(String.format("This entity is in-range: [%s]",entityId)).toSolve((BeliefState belief) -> belief.withinRange(entityId));

        //define the goal structure
        Goal g = goal.withTactic(FIRSTof( //the tactic used to solve the goal
                TacticLib.navigateTo(entityId),//try to move to the entity
                TacticLib.explore(),//find the entity
                ABORT()));
        return g;
    }
    
    
    /**
     * This is used by the method entityIsInRange_smarter below. It defined when
     * the agent is close enough to make a fresh observation. The distance should
     * be larger than the "vicinity" distance, but close enough to make sure that
     * the agent will actually have line of sight to the entity.
     * The setting of these two distances are a bit SENSITIVE.
     */
    private static boolean inClosexxxRange(BeliefState belief, String entityId) {
    	if (belief.position==null) return false ;
    	var e = belief.getEntity(entityId) ;
    	if (e==null) return false ;
    	return belief.position.distance(e.position) <= BeliefState.UNIT_DISTANCE  ;
    }
    
    /**
     * Use this goal to approach a door which according to the current belief is closed,
     * but the agent has a reason to suspect that it might be open now (e.g. because
     * it just pushed on a button that it thinks might open the door).
     * The agent cannot literally navigate to the door, because this would be prohibited
     * by its current navigation graph (which will say that the door is unreachable, and
     * therefore cannot provide a path to it).
     * Instead, this goal will check if there is a point in the VICINITY of the door that
     * are reachable (for now this is either a point some vicinity-distance E/W/N or S
     * of the position/center of the door. If such a vicinity-point can be found, the
     * agent will navigate to that point. It will actually stop a bit before that point,
     * in just enough distance (decided by inClosexxxRange) to make an updated observation
     * on the state of the door (whether it is open or close).
     */
    public static GoalStructure entityIsInRange_smarter(String entityId) {
        Goal goalE = goal(String.format("East vicinity of this entity is in-range: [%s]",entityId))
        		    . toSolve((BeliefState belief) -> inClosexxxRange(belief,entityId));
        var goalW = goal(String.format("West vicinity of this entity is in-range: [%s]",entityId))
        		   . toSolve((BeliefState belief) -> inClosexxxRange(belief,entityId));
        var goalN = goal(String.format("North vicinity of this entity is in-range: [%s]",entityId))
        		    . toSolve((BeliefState belief) -> inClosexxxRange(belief,entityId));
        var goalS = goal(String.format("South vicinity of this entity is in-range: [%s]",entityId))
        		   . toSolve((BeliefState belief) -> inClosexxxRange(belief,entityId));
        
        // the setting of this parameter is sensitive; 0.7 seems to work
        double shift = 0.7*BeliefState.UNIT_DISTANCE ;
        
        var g1 = FIRSTof(
        		    goalE . withTactic(FIRSTof(navigateToVicinity(entityId, new Vec3(shift,0,0)), 
        		    		           ABORT()))
        		          . lift(),
        		    goalW . withTactic(FIRSTof(navigateToVicinity(entityId, new Vec3(-shift,0,0)),
        		    		           ABORT()))
        		          . lift(),
        		    goalN . withTactic(FIRSTof(navigateToVicinity(entityId, new Vec3(0,0,shift)),
        		    		           ABORT()))
        		          . lift(),
        		    goalS . withTactic(FIRSTof(navigateToVicinity(entityId, new Vec3(0,0,-shift)),
        		    		           ABORT()))
        		          . lift()
        		  ) ;
        
        var goal = FIRSTof(
        		     entityIsInRange(entityId).lift(),
        		     SEQ(g1, entityIsInRange(entityId).lift())
        		   ) ;
        
        return goal ;
    }

    private static Tactic navigateToVicinity(String id, Vec3 shift) {
    	// replace the guard with this one:
    	return TacticLib.actionNavigateTo(id)
    		   . on((BeliefState belief) -> { 
    			    if(belief.position == null) return null ;
    		        var e = belief.getEntity(id);
    		        if (e == null) return null ; //guard
    		        // have to clone shift to avoid unintended accumulating plus:
    		        var newPosition = new Vec3(shift.x,shift.y,shift.z) ;
    		        newPosition.add(e.position);
    		        //System.out.println(">>>> " + id + " @" + e.position) ;
    		        Vec3[] path = belief.cachedFindPathTo(newPosition);
    		        if(path == null) return null;//if there is no path return null
    		        return new Tuple(newPosition, path) ;
    		   }).lift() ;
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
            subGoals[i] = entityIsInRange(entitiesIds[i]).lift();
        }
        return SEQ(subGoals);
    }

    /**
     * Construct a goal structure that will make an agent to move towards the given entity and interact with it.
     * Currently the used tactic is not smart enough to handle a moving entity, in particular if it moves while
     * the agent is entering the interaction distance.
     * 
     * The goal fails if the agent no longer believes that the entity is reachable, or when it fails to 
     * interact with it.
     *
     * @param entityId The entity to walk to and interact with
     * @return A goal structure
     * @Incomplete: this goal should check if the object has a given desired state, and perhaps include a position check in the goal predicate itself
     */
    public static GoalStructure entityIsInteracted(String entityId) {
        //move to the object
        Goal goal1 = goal(String.format("This entity is in interaction distance: [%s]", entityId)).toSolve((BeliefState belief) ->
                belief.position != null && belief.canInteractWith(entityId));

        //interact with the object
        Goal goal2 = goal(String.format("This entity is interacted: [%s]", entityId)).toSolve((BeliefState belief) -> true);

        //Set the tactics with which the goals will be solved
        GoalStructure g1 = goal1.withTactic(FIRSTof( //the tactic used to solve the goal
                TacticLib.navigateTo(entityId), //try to move to the enitity
                TacticLib.explore(), //find the entity
                SEQ(TacticLib.forcePastExploreBlindCorner(),
                    FIRSTof(TacticLib.navigateTo(entityId),
                            ABORT())),
                ABORT())) //find the agent's own position
                .lift();
        GoalStructure g2 = goal2.withTactic(FIRSTof( //the tactic used to solve the goal
                TacticLib.interact(entityId),// interact with the entity
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
        for (int i = 0; i < buttons.length; i++) gs[i] = entityIsInteracted(buttons[i]);
        gs[buttons.length] = entityIsInRange(goal).lift();

        return SEQ(gs);
    }

    /**
     * This goal will make agent to navigate to the given entity, and make sure that the agent has the 
     * latest observation of the entity.
     * Getting the entity within sight is enough to complete this goal.
     * 
     * This goal fails if the agent no longer believes that the entity is reachable.
     */
    public static Goal entityInspected(String id){
        return goal("This entity is inpected: " + id)
                .toSolve((BeliefState b) -> b.evaluateEntity(id, e -> e.lastUpdated == b.lastUpdated))
                .withTactic(FIRSTof(
                        TacticLib.navigateTo(id),
                        TacticLib.explore(),
                        SEQ(TacticLib.forcePastExploreBlindCorner(),
                            FIRSTof(TacticLib.navigateTo(id),
                            		ABORT())),
                        ABORT()));
    }

    /**
     * This goal will make agent to navigate to the given entity, and make sure that the state of the
     * entity satisfies the given predicate.
     * 
     * The goal fail if the agent no longer believes that the entity is reachable, or when the
     * predicate is not satisfied when it is observed.
   
     * @param id: entityId
     * @return Goal
     */
    public static GoalStructure entityInspected(String id, Predicate<Entity> predicate){
        return SEQ(
            entityInspected(id).lift(),
            goal("Evaluate " + id)
            .toSolve((BeliefState b) -> b.evaluateEntity(id, predicate))
            .withTactic(FIRSTof(
                    TacticLib.observeOnce(),
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
            testgoal("Invariant check " + id, agent)
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
                    TacticLib.observeOnce(),
                    ABORT())).lift()
        );
    }
    
    /**
     * Create a test-goal to check the state of the game, whether it satisfies the given predicate.
     * Internally, this goal will first spend one tick to get a fresh observation, then at the next tick it
     * will do the checking.
     */ 
    public static GoalStructure invariantChecked(TestAgent agent, String info, Predicate<BeliefState> predicate){
        return SEQ(
            testgoal("Evaluate " + info, agent)
            .toSolve((BeliefState b) -> true) // nothing to solve
            .invariant(agent,                 // something to check :)
            		(BeliefState b) -> {
            			if (predicate.test(b))
            			   return new VerdictEvent("Inv-check", info, true) ;
            			else 
            			   return new VerdictEvent("Inv-check" , info, false) ;
            		    }
            		)
            .withTactic(FIRSTof(
                    TacticLib.observeOnce(),
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
                TacticLib.shareMemory(id)
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
                TacticLib.sendPing(idFrom, idTo)
        );
    }
}
