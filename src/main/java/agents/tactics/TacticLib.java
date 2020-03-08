/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.tactics;

import communication.agent.AgentCommandType;
import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.multiAgentSupport.Acknowledgement;
import nl.uu.cs.aplib.multiAgentSupport.Message;
import world.BeliefState;
import world.InteractiveEntity;
import world.Observation;

import java.util.Arrays;

import static nl.uu.cs.aplib.AplibEDSL.action;

/**
 * This class provide a set of standard tactics to interact with the Lab Recruits
 * game. Tactics are higher-level actions, and are used to solve goals for
 * aplib agents.
 */
public class TacticLib {
    /**
     * This method will return a tactic in which the agent will move to a given known position, only enabled if the
     * agent knows its own position
     * @param position: The position to move to
     * @return The tactic in which the agent will move to the known position
     */
    public static Tactic navigateTo(Vec3 position) {
        Tactic move = action("Navigate to " + position.toString())
                .do2((BeliefState belief) -> (Vec3[] path) -> {
                    //if there is no path, set the path
                    if(belief.getGoalLocation() == null){
                        belief.mentalMap.applyPath(position, path);
                    }

                    //only reset the path if the goal has changed
                    if(belief.getGoalLocation().distance(position) > 0.01){
                        belief.mentalMap.applyPath(position, path);
                    }
                    Observation o = belief.env().moveToward(belief.id, belief.position, belief.getNextWayPoint());//move towards the next way point
                    belief.markObservation(o);
                    return belief;
                }).on((BeliefState belief) -> {
                    if(belief.position == null) return null;//guard
                    return belief.navigate(position);//find a path towards the target position
                }).lift();
        return move;
    }

    /**
     * This method will return a tactic in which the agent will move towards an
     * in-game entity with a given id. This action is
     * only enabled if the object with this id is present in the belief state
     * @param id: The id of the object to which the agent wants to move
     * @return The tactic in which the agent will try to move to the entity
     */
    public static Tactic navigateTo(String id) {
        Tactic move = action("Navigate to " + id)
                .do2((BeliefState belief) -> (Tuple<Vec3, Vec3[]> p) -> {
                    //if there is no path, set the path
                    if(belief.getGoalLocation() == null){
                        belief.mentalMap.applyPath(p.object1, p.object2);
                    }

                    //only reset the path if the goal has changed
                    if(belief.getGoalLocation().distance(p.object1) > 0.01){
                        belief.mentalMap.applyPath(p.object1, p.object2);
                    }
                    Observation o = belief.env().moveToward(belief.id, belief.position, belief.getNextWayPoint());//move towards the next way point
                    belief.markObservation(o);
                    return belief;
                }).on((BeliefState belief) -> {
                    var e = belief.getEntity(id);
                    if (e == null) {
                        var ie = belief.getInteractiveEntity(id);
                        if(ie != null) e = ie;
                    }
                    if(belief.position == null || e == null) return null;//guard
                    Vec3[] path = belief.navigate(e.position);

                    if(path == null) return null;//if there is no path return null

                    return new Tuple(e.position, path);//return the path finding information
                }).lift();
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
                    belief.markObservation(o);
                    return belief;
                }).on_((BeliefState belief) -> belief.position != null && belief.canInteract(objectID))
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
                    belief.markObservation(o);
                    return belief;
                }).lift();
        return observe;
    }

    public static Tactic observeOnce() {
        //this is a wait action which will allow the agent to retrieve an observation
        Tactic observe = action("Observe once")
                .do1((BeliefState belief) -> {
                    Observation o = belief.env().observe(belief.id);
                    belief.markObservation(o);
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
                    belief.markObservation(o);

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
                    belief.markObservation(o);
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
                    belief.markObservation(o);

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
                    belief.markObservation(o);

                    //return whether we have received an observation yes or no
                    return belief;
                })
                .lift() ;
    }

    /**
     * This method will return a tactic in which the agent will explore the world
     * @return The tactic in which the agent will seek the object
     */
    public static Tactic explore() {
        var explore = action("Explore")
                .do2((BeliefState belief) -> (Tuple<Vec3, Vec3[]> p) -> {
                	//System.out.println("### explore") ;
                    //if the agent already has a goal move towards that goal
                    if(belief.getGoalLocation() != null){
                        Observation o = belief.env().moveToward(belief.id, belief.position,belief.getNextWayPoint());//move towards the next way point
                        belief.markObservation(o);
                    } else {
                        //explore the closest unknown node
                        //if there is no path, set the path
                        if(belief.getGoalLocation() == null){
                            belief.mentalMap.applyPath(p.object1, p.object2);
                        }

                        //only reset the path if the goal has changed
                        if(belief.getGoalLocation().distance(p.object1) > 0.01){
                            belief.mentalMap.applyPath(p.object1, p.object2);
                        }
                        Observation o = belief.env().moveToward(belief.id, belief.position, belief.getNextWayPoint());//move towards the next way point
                        belief.markObservation(o);
                    }
                    return belief;
                }).on((BeliefState belief) -> {
                    //guard if the position of the agent is not known
                    if(belief.position == null) return null;
                    //get the location of the closest unexplored node
                    Vec3 g = belief.getUnknownNeighbourClosestTo(belief.position, belief.position);
                    if(g == null) return null;

                    //check if we can find a path
                    Vec3[] path = belief.navigate(g);
                    if(path == null) return null;
                    //System.out.println("### find unexplored " + g + ", current pos: " + belief.position) ;
                    return new Tuple(g, path);//return the path finding information
                }).lift();
        return explore;
    }

}
