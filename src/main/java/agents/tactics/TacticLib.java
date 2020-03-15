/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.tactics;

import communication.agent.AgentCommandType;
import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.Action;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.multiAgentSupport.Acknowledgement;
import nl.uu.cs.aplib.multiAgentSupport.Message;
import world.BeliefState;
import world.*;
import world.Observation;

import java.util.Arrays;

import static nl.uu.cs.aplib.AplibEDSL.*;

/**
 * This class provide a set of standard tactics to interact with the Lab Recruits
 * game. Tactics are higher-level actions, and are used to solve goals for
 * aplib agents.
 */
public class TacticLib {
   
	/**
	 * This method will return a tactic in which the agent will move towards a given
	 * position. This action is if the agent believes that there is a path (through
	 * the navigation-map maintained by the agent) to the entity. Note that in
	 * reality the entity may turn out to be unreachable. The agent will try to
	 * detect if it gets stuck (e.g. if the position turns out to be unreachable),
	 * in which case it will check again if a path to the position exists, according
	 * to the latest information it has. If so, the agent will follow this path, and
	 * else the tactic is not enabled.
	 */
    public static Tactic navigateTo(Vec3 position) {
        Tactic move = action("Navigate to " + position.toString())
                .do2((BeliefState belief) -> (Vec3[] path) -> {
                	//System.out.println("### tactic NavigateTo " + position) ;
                    //if there is no path, set the path
                    if(belief.getGoalLocation() == null){
                        belief.mentalMap.applyPath(position, path);
                    }
                    else {
                    	//reset the path if the goal has changed
                        if(belief.getGoalLocation().distance(position) > 0.01){
                            belief.mentalMap.applyPath(position, path);
                        }
                    }
                    //move towards the next way point
                    Observation o = belief.moveToward(belief.getNextWayPoint());
                    belief.updateBelief(o);
                    // handle when the agent gets stuck:
                    if (belief.isStuck(position)) {
                    	tryToUnstuck(belief) ;
                    }
                    return belief;
                }).on((BeliefState belief) -> {
                    if(belief.position == null) return null;//guard
                    return belief.cachedFindPathTo(position);//find a path towards the target position
                }).lift();
        return move;
    }
    
    public static Tactic originalNavigateTo(Vec3 position) {
        Tactic move = action("Move to " + position.toString())
                .do2((BeliefState belief) -> (Vec3[] path) -> {
                    //if there is no path, set the path
                    if(belief.getGoalLocation() == null){
                        belief.mentalMap.applyPath(position, path);
                    }

                    //only reset the path if the goal has changed
                    if(belief.getGoalLocation().distance(position) > 0.01){
                        belief.mentalMap.applyPath(position, path);
                    }
                    Observation o = belief.moveToward(belief.getNextWayPoint());//move towards the next way point
                    belief.updateBelief(o);
                    return belief;
                }).on((BeliefState belief) -> {
                    if(belief.position == null) return null;//guard
                    return belief.cachedFindPathTo(position);//find a path towards the target position
                }).lift();
        return move;
    }
    
    // to be called when the agent seems to get stuck; this will try to unstuck the
    // agent in the next few updates:
    private static void tryToUnstuck(BeliefState belief) {
    	System.out.println("#### STUCK @" + belief.position + ", V=" + belief.derived_lastNonZeroXZVelocity()) ;
    	if (belief.atDoor() != null) {
    		// if the agent is stuck at a door, that is probably because the door is closed,
    		// and the agent's belief is outdated. We will clear the agent goal-
    		// position and the calculated path to to, to force fresh path calculation
    		// at the next update. This may unstuck the agent, if there is another path to the
    		// target. But if there is no path, this will basically cause the tactic that calls
    		// this to be disabled at the next update.
    		System.out.println("#### stuck reason: probably door. Forcing a new path calculation...") ;
    		belief.mentalMap.clearGoalLocation(); 
    	}
    	else {
    		// else the agent is likely to be be stuck. Try to get a nearby position
    		// the agent can move to to unstuck itself:
    		System.out.println("#### stuck reason: probably cannot get past a turn-corner.") ;
    		var unstuckPosition = belief.unstuck() ;
    		if (unstuckPosition != null) {
    			System.out.println("#### forcing a move past the corner...to " + unstuckPosition) ;        		
    			belief.mentalMap.insertNewWayPoint(unstuckPosition);
    		}
    		else {
    			// else .... for now do nothing :|
        		System.out.println("#### unfortunately cannot find a solution...") ; 
    		}	       			
    	}
    	belief.clearStuckTrackingInfo();
    }
    
    /***
     * Sometimes the agent can become stuck in a turn around corner while
     * it is searching for a position or entity. When the entity is not on
     * the known map, the search is typically combined with exploration.
     * It can be the case, that in a turn around corner the agent has seen
     * the last navigation polygons, and therefore it has no more node to
     * explore,  but its line of sight to the target is blocked, and 
     * therefore it mistakenly concludes that the entity could not exist.
     * 
     * This tactic will try to puch the agent to move a little bit past the
     * stuck corner.
     */
    public static Tactic forcePastExploreBlindCorner() {
    	return action("Trying to force the agent to get past an explore-blind-corner")
    		   . do1((BeliefState belief) -> { 
    			    var unstuckPosition = belief.unstuck() ;
    			    System.out.println("#### invoking forcePastExploreBlindCorner") ;
    			    System.out.println("#### agent velocity " + belief.derived_lastNonZeroXZVelocity()) ;
    			    System.out.println("#### unstuck option " + unstuckPosition) ;  
    			    Observation o ;
    			    if (unstuckPosition!=null)
    			    	o = belief.moveToward(unstuckPosition) ;
    			    else 
    			    	o = belief.env().observe(belief.id) ;
    			    belief.updateBelief(o);
    			    return belief ; 
    			 })
    		   . on(belief -> true)
    		   . lift() ;
    }
    
    

	/**
	 * This method will return a tactic in which the agent will move towards an
	 * in-game entity with a given id. This action is only enabled if the object
	 * with this id is present in the belief state, and furthermore the agent
	 * believes that there is a path (through the navigation-map maintained by the
	 * agent) to the entity. Note that in reality the entity may turn out to be
	 * unreachable. The agent will try to detect if it gets stuck (e.g. if the
	 * entity turns out to be unreachable), in which case it will check again if
	 * a path to the entity exists, according to the latest information it has.
	 * If so, the agent will follow this path, and else the tactic is not enabled.
	 */
    public static Tactic navigateTo(String id) { return actionNavigateTo(id).lift(); }
    
    public static Action actionNavigateTo(String id) {
        Action move = action("Navigate to " + id)
                .do2((BeliefState belief) -> (Tuple<Vec3, Vec3[]> p) -> {
                    //if there is no path, set the path
                    if(belief.getGoalLocation() == null){
                        belief.mentalMap.applyPath(p.object1, p.object2);
                    }
                    else {
                    	//reset the path if the goal has changed
                        if(belief.getGoalLocation().distance(p.object1) > 0.01){
                            belief.mentalMap.applyPath(p.object1, p.object2);
                        }
                    }
                    Observation o = belief.moveToward(belief.getNextWayPoint());//move towards the next way point
                    belief.updateBelief(o);
                    // handle when the agent gets stuck:
                    if (belief.isStuck(p.object1)) {
                    	tryToUnstuck(belief) ;
                    }
                    return belief;
                }).on((BeliefState belief) -> {
                	if(belief.position == null) return null ;
                    var e = belief.getEntity(id);
                    if (e == null) return null ; //guard
                    Vec3[] path = belief.cachedFindPathTo(e.position);
                    if(path == null) return null;//if there is no path return null
                    return new Tuple(e.position, path);//return the path finding information
                }) ;
        return move;
    }
    
    void calculateDoorAlternativePositions(InteractiveEntity door) {
    	var center = door.center ;
    	boolean onXdirection = door.extents.x > door.extents.z ;
    }

    /**
     * Send an interact command if the agent is close enough
     * @param objectID The id of the in-game entity to interact with
     * @return A tactic in which the agent will interact with the object
     */
    public static Tactic interact(String objectID) {
        Tactic interact = action("Interact").
                do1((BeliefState belief) -> {
                    Observation o = belief.env().interactWith(belief.id, objectID);
                    belief.updateBelief(o);
                    return belief;
                })
                .on_((BeliefState belief) -> belief.position != null && belief.canInteractWith(objectID))
                .lift();
        return interact;
    }

    /**
     * This method will return an observe tactic which will do nothing but receive an observation and update the agent
     * @return A do nothing action
     */
    public static Tactic observe() {
        //this is a wait action which will allow the agent to retrieve an observation
        Tactic observe = action("Observe")
                .do1((BeliefState belief) -> {
                    Observation o = belief.env().observe(belief.id);
                    belief.updateBelief(o);
                    return belief;
                }).lift();
        return observe;
    }

    public static Tactic observeOnce() {
        //this is a wait action which will allow the agent to retrieve an observation
        Tactic observe = action("Observe once")
                .do1((BeliefState belief) -> {
                    Observation o = belief.env().observe(belief.id);
                    belief.updateBelief(o);
                    return belief;
                }).on((BeliefState b) -> !b.didNothingPreviousTurn).lift();
        return observe;
    }

    /**
     * This tactic will allow an agent to share its memory of explored nodes and make an observation
     * @param id: The id of the sender
     * @return A tactic in which the agent broadcasts its memory of the navmesh
     */
    public static Tactic shareMemory(String id){
        return action("Share memory")
                . do1((BeliefState belief)-> {
                    //do an observation
                    Observation o = belief.env().observe(belief.id);
                    belief.updateBelief(o);

                    //send the message
                    Acknowledgement a = belief.messenger().send(id,0, Message.MsgCastType.BROADCAST, "","MemoryShare", (Integer[])belief.mentalMap.getKnownVerticesById()) ;
                    return belief;
                }).lift();
    }

    /**
     * This tactic cause the agent to receive an memory share if one is available and make an observation
     * @return The tactic which will receive the memory share
     */
    public static Tactic receiveMemoryShare(){
        return action("Receive memory share")
                . do1((BeliefState belief)-> {
                    //get the memory share messages
                    Message m = belief.messenger().retrieve(M -> M.getMsgName().equals("MemoryShare")) ;
                    while(m != null){
                        //apply the memory share
                        int[] intArray = Arrays.stream(Integer[].class.cast(m.getArgs())).mapToInt(Integer::intValue).toArray();
                        belief.mentalMap.updateKnownVertices(intArray);

                        //check if there is another memory share
                        m = belief.messenger().retrieve(M -> M.getMsgName().equals("MemoryShare")) ;
                    }

                    //do an observation
                    Observation o = belief.env().observe(belief.id);
                    belief.updateBelief(o);
                    return belief;
                })
                .on_((BeliefState S) -> S.messenger().has(M -> M.getMsgName().equals("MemoryShare")))//check if there is a memory share available
                .lift() ;
    }

    /**
     * This tactic will allow an agent to share its memory of explored nodes and make an observation
     * @param idFrom: The id of the sender
     * @param idTo: The id of the receiver
     * @return A tactic in which the agent broadcasts its memory of the navmesh
     */
    public static Tactic sendPing(String idFrom, String idTo){
        return action("Send ping")
                . do1((BeliefState belief)-> {
                    //do an observation
                    Observation o = belief.env().observe(belief.id);
                    belief.updateBelief(o);

                    //send the message
                    Acknowledgement a = belief.messenger().send(idFrom,0, Message.MsgCastType.SINGLECAST, idTo,"Ping", "") ;
                    return belief;
                }).lift();
    }

    /**
     * This tactic cause the agent to receive a ping if one is available and make an observation
     * @return The tactic which will receive the ping returns true or false on depended on whether there was an ping yes or no
     */
    public static Tactic receivePing(){
        return action("Receive ping")
                . do1((BeliefState belief)-> {
                    //get the ping message
                    Message m = belief.messenger().retrieve(M -> M.getMsgName().equals("Ping"));

                    //update the belief state
                    if(m != null) belief.receivedPing = true;

                    //do an observation
                    Observation o = belief.env().observe(belief.id);
                    belief.updateBelief(o);

                    //return whether we have received an observation yes or no
                    return belief;
                })
                .lift() ;
    }

    /**
     * This method will return a tactic in which the agent will "explore" the world.
     * The tactic will locate the nearest reachable navigation node which the agent
     * has not discovered yet, and drive the agent to go there.
     */
    public static Tactic explore() {
        var explore = action("Explore")
                .do2((BeliefState belief) -> (Tuple<Vec3, Vec3[]> p) -> {
                	//System.out.println("### tactic explore") ;
                    if(belief.getGoalLocation() != null){
                    	// reset the path if the goal has changed:
                    	if(belief.getGoalLocation().distance(p.object1) > 0.01){
                            belief.mentalMap.applyPath(p.object1, p.object2);
                        }	
                    } else {
                    	//if the agent does not have a goal, set the given path p as the goal to follow
                    	
                    	// WP: this check is unnecessary... suprressing it
                        //    explore the closest unknown node
                        //    if there is no path, set the path
                        //if(belief.getGoalLocation() == null){
                        belief.mentalMap.applyPath(p.object1, p.object2);
                        
                        // WP : this won't change since we have already applied the path above! 
                        // Moving it to the then branch:
                        //    only reset the path if the goal has changed
                        //if(belief.getGoalLocation().distance(p.object1) > 0.01){
                        //    belief.mentalMap.applyPath(p.object1, p.object2);
                        //}
                    }
                    //move towards the next way point
                    Observation o = belief.moveToward(belief.getNextWayPoint());
                    belief.updateBelief(o);
                    return belief;
                }).on((BeliefState belief) -> {
                    //guard if the position of the agent is not known
                    if(belief.position == null) return null;
                    //get the location of the closest unexplored node
                    Vec3 g = belief.getUnknownNeighbourClosestTo(belief.position, belief.position);
                    if(g == null) {
                    	System.out.println("### cannot find new node; agent is @" + belief.position) ;
                    	return null;
                    }

                    //check if we can find a path
                    Vec3[] path = belief.cachedFindPathTo(g);
                    if(path == null) {
                    	System.out.println("### cannot find path; agent is @" + belief.position) ;
                    	return null;
                    }
                    //System.out.println("### find unexplored " + g + ", current pos: " + belief.position) ;
                    return new Tuple(g, path);//return the path finding information
                }).lift();
        return explore;
    }

}
