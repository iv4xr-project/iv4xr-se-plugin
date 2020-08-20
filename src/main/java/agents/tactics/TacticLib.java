/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.tactics;

import static nl.uu.cs.aplib.AplibEDSL.*;
import nl.uu.cs.aplib.mainConcepts.Action;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.multiAgentSupport.Acknowledgement;
import nl.uu.cs.aplib.multiAgentSupport.Message;
import nl.uu.cs.aplib.agents.MiniMemory;
import nl.uu.cs.aplib.utils.Pair;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import eu.iv4xr.framework.spatial.meshes.Face;

import world.*;

import java.util.*;

/**
 * This class provide a set of standard tactics to interact with the Lab
 * Recruits game. While {@link world.LabWorldModel} (and likewise the underlying
 * {@link environments.LabRecruitsEnvironment}) provides the primitive
 * methods/actions to control the Lab Recruit game, the tactics provided by this
 * class TacticLib provide higher-level actions, and are used to solve goals for aplib
 * agents. Most tactics provided here incorporate automatic path finding over
 * the Lab Recruits game world. This path finding will in principle allow an
 * agent to auto navigate from its current position to some position p,
 * provided: (1) p is physically reachable from the agent's current position,
 * and (2) the agent believes that p is reachable. For example, if p is located
 * in a room that the agent never sees, it would then believe that it has no way
 * to reach p. So, before it is able to auto-navigate to p, we may need to first
 * convince the agent of the reachability of p, e.g. by making it see that the
 * areas in-between are reachable.
 */
public class TacticLib {
   
	/**
	 * The same as {@link rawNavigateTo}, but the constructed tactic will also try
	 * to get the agent unstucked if it thinks it has become stuck. So, the method
	 * constructs a tactic T that will drive the agent to move towards a given
	 * position using a path planning algorithm. The tactic T is enabled (more
	 * precisely: the action that underlies T) if the agent BELIEVES that there is a
	 * path (through the navigation-graph maintained by the agent) to the entity.
	 * Note that agents' view are limited, so what they believe may not be what
	 * reality is. In other words, the position might actually be unreachable. As
	 * the agent tries to reach it, eventually it will discover that it is
	 * unreachable though, after which the T will no longer be enabled.
	 * 
	 * Similarly, the position might actually be reachable, but the agent believes
	 * it is not, and therefore T will not be enabled. In this case you will first
	 * need to make the agent explore so that it can update itself with a more
	 * recent information from which the agent can infer that the position is
	 * reachable.
	 * 
	 * This tactic will try to detect if the agent has become stuck and try to
	 * unstuck it.
	 */
	public static Tactic navigateTo(Vec3 position) {
		return FIRSTof(
				 forceReplanPath(), 
				 tryToUnstuck(),
				 rawNavigateTo(position)
			   )  ;
	}
	
	
	/**
	 * To navigate to the location of an in-game element. Be mindful that the destination
	 * location is literally the location of the given game element. E.g. if it is a 
	 * closed door, trying to literally get to its position won't work, since that position
	 * is blocked. 
	 */
	public static Tactic navigateTo(String id) {
		return FIRSTof(
				 forceReplanPath(), 
				 tryToUnstuck(),
				 rawNavigateTo(id)
			   )  ;
	}
	
	
	static Tactic navigateToClosestReachableNode(String id) {
		
		MiniMemory memory = new MiniMemory("S0") ;
		
		Action move = 
				  rawNavigateTo_("Navigate to a navigation vertex nearby " + id, null)
				  
				. on((BeliefState belief) -> {
					
					LabEntity e = belief.worldmodel.getElement(id) ;
    			    if (e==null) return null ;
    			    
					Vec3 nodeLocation = null ;
					if (!memory.memorized.isEmpty()) {
						nodeLocation = (Vec3) memory.memorized.get(0) ;
					}
					Vec3 currentGoalLocation = belief.getGoalLocation() ;
					
					if (nodeLocation == null 
					    || currentGoalLocation == null
					    || Vec3.dist(nodeLocation,currentGoalLocation) >= 0.05) {
						// in all these cases we need to calculate the node to go
						
    			    var entity_location = e.getFloorPosition() ;
	    			    List<Pair<Vec3,Float>> candidates = new LinkedList<>() ;
	    			    int N = belief.pathfinder.vertices.size() ;
	    			    int k=0 ;
	    			    for (Vec3 v : belief.pathfinder.vertices) {
	    			    	if (belief.pathfinder.seenVertices.get(k)) {
	    			    		// v has been seen:
	    			    		candidates.add(new Pair(v, Vec3.dist(entity_location, v))) ;
	    			    	}
	    			    	k++ ;
	    			    }
		    			    
		    			if (candidates.isEmpty()) return null ;
		    			// sort the candidates according to how close they are to the entity e (closest first)
		    			candidates.sort((c1,c2) -> c1.snd.compareTo(c2.snd));
		    			// now find the first one that is reachable:
		    		    System.out.println(">>> #candidates closest reachable neighbor nodes = " + candidates.size()) ;
		    			Vec3 destination = null ;
		    			List<Vec3> path = null ;
		    			for(var c : candidates) {
		    			    destination = c.fst ;
		    			    path = belief.findPathTo(destination,true) ;
		    			    if (path != null) {
		    			    	// found a reachable candidate!
		    			    	System.out.println(">>> a reachable nearby node found :" + destination + ", path: " + path) ;
		    			    	memory.memorized.clear();
		    			    	memory.memorize(destination);
		    			    	return new Pair(destination,path) ;
		    			    }
		    		    }
		    			System.out.println(">>> no reachable nearby nodes :|") ;
		    			// no reachable node can be found. We will clear the memory, and declare the tactic as disabled
		    			memory.memorized.clear() ;
		    			return null ;
					}
					else {
						// else the memorized location and the current goal-location coincide. No need to
						// recalculate the path, so we will just return the pair (memorized-loc,null)
						return new Pair (nodeLocation,null) ;
					}
				}) ;
		
		return move.lift() ;
	}
	
    /**
     * A tactic to navigate to the given entity's location. The tactic is enabled if 
     * the agent believes the entity exists and is reachable. Else the tactic is NOT 
     * enabled.
     */
    public static Tactic rawNavigateTo(String id) { 
    	
    	// let's just reuse rawNavigateTo_(..), and then we replace its guard:
    	
    	Action move = rawNavigateTo_("Navigate to " + id, null)
    			      // replacing its guard with this new one:
		              . on((BeliefState belief) -> {
		                	var e = (LabEntity) belief.worldmodel.getElement(id) ;
		    			    if (e==null) return null ;
		    			    var p = e.getFloorPosition() ;
		                	Vec3 currentDestination = belief.getGoalLocation() ;
		                	//System.out.println(">>> navigating to " + id) ;
		                	if (currentDestination==null || Vec3.dist(currentDestination,p) >= 0.05) {
		                		// the agent has no current location to go to, or the new goal location
		                		// is quite different from the current goal location, we will then calculate
		                		// a new path:
		                		var path = belief.findPathTo(p,true) ; // force to calculate path
		                		//System.out.println(">>> currentDestination: " + currentDestination 
		                		//		           + ", #path: " + path.length) ;
		                		if (path==null) return null ;
		                		return new Pair(p,path) ;
		                	}
		                	else {
		                		// the agent is already going to the specified location. So there is
		                		// no need to calculate a new path. We will the pair (p,null)
		                		// to signal this.
		                		return new Pair(p,null) ;
		                	}}) ;
    	return move.lift() ;
     }
    
	/**
	 * Construct a tactic T that will drive the agent to move towards a given
	 * position using a path planning algorithm. The tactic T is enabled (more
	 * precisely: the action that underlies T) if the agent BELIEVES that there is a
	 * path (through the navigation-graph maintained by the agent) to the entity;
	 * otherwise the tactic is NOT enabled.
	 * 
	 * Note that agents' view are limited, so what they believe may not be what
	 * reality is. In other words, the position might actually be unreachable. As
	 * the agent tries to reach it, eventually it will discover that it is
	 * unreachable though, after which the T will no longer be enabled.
	 * 
	 * Similarly, the position might actually be reachable, but the agent believes
	 * it is not, and therefore T will not be enabled. In this case you will first
	 * need to make the agent explore so that it can update itself with a more
	 * recent information from which the agent can infer that the position is
	 * reachable.
	 * 
	 * This tactic will not try to detect if the agent has become stuck.
	 */
    public static Tactic rawNavigateTo(Vec3 position) {
    	return rawNavigateTo_("Navigate to " + position, position) . lift() ;
    }
    
    // a generic navigateTo that we will later reuse and tweak in a number of 
    // navigate-like tactics
    private static Action rawNavigateTo_(String actionName, Vec3 position) {
    	Action move = action(actionName)
                .do2((BeliefState belief) -> (Pair<Vec3,List<Vec3>> q)  -> {
                	// q is a pair of (distination,path). Passing the destination is not necessary
                	// for this tactic, but it will allows us to reuse the effect
                	// part for other similar navigation-like tactics
                	var destination = q.fst ;
                	var path = q.snd ;
                	
                	//System.out.println("### tactic NavigateTo " + destination) ;
                    
                	//if a new path is received, memorize it as the current path to follow:
                	if (path!= null) {
                		belief.applyPath(belief.worldmodel.timestamp, destination, path) ;
                	}               
                    //move towards the next way point of whatever the current path is:
                    belief.worldmodel.moveToward(belief.env(),belief.getNextWayPoint());
                    return belief; 
                    })
                .on((BeliefState belief) -> {
                	Vec3 currentDestination = belief.getGoalLocation() ;
                	if (currentDestination==null || Vec3.dist(currentDestination,position) >= 0.05) {
                		// the agent has no current location to go to, or the new goal location
                		// is quite different from the current goal location, we will then calculate
                		// a new path:
                		var path = belief.findPathTo(position,true) ; // force to calculate path
                		if (path==null) return null ;
                		return new Pair(position,path) ;
                	}
                	else {
                		// the agent is already going to the specified location. So there is
                		// no need to calculate a new path. We will return a pair(position,null)
                		// to signal this.
                		return new Pair(position,null) ;
                	}}) ;
    	return move ;
    }
    
    
    /**
     * When asked to get to a certain (far away) position, the agent would need
     * multiple update rounds to get to the goal position. To do this, it first
     * ask a path to be calculated (some pathfinding algorithm is used to do this).
     * If a path can be found, the agent will store this in its memory and proceed
     * to follow this path. It may happen that this path leads to a door that
     * turns out to be closed; so, path re-calculation is needed.
     * 
     * This method constructs a tactic T that tries to identify when recalculation of 
     * path to the the goal position, namely when:
     * 
     *   (1) the agent is near a door that turns out to be closed, and hence blocking 
     *        the path that was previously planned.
     *        
     *    and (2) the path is not recently calculated:
     *
     *  Path recalculation is forced by clearing the goal-position.	
     */
    public static Tactic forceReplanPath() {
        Tactic clearTargetPosition = action("Force path recalculation.")
                .do1((BeliefState belief) -> {
                	System.out.println("#### Forcing path recalculation @" + belief.worldmodel.position) ;
                	belief.clearGoalLocation();
                	return belief ;
                })
                .on_((BeliefState belief) -> { 
                	if (belief.getGoalLocation() == null
                		|| belief.worldmodel.timestamp - belief.getGoalLocationTimestamp() < 4) {
                		return false ;
                	}
                	var closeby_doors = belief.closebyDoor() ;
                	//System.out.println(">>> #close-by doors: " + closeby_doors.size()) ;
            		for (var door : closeby_doors) {
                		if (!belief.isOpen(door)) {
                			return true ;
                		}
                	}		
                	return false ; })
                .lift() ;
           return clearTargetPosition ;     
    }
    /**
     * This tactic detects if the agent gets stuck (e.g. if the position turns out to be unreachable),
	 * in which case it will check again if a path to the position exists, according
	 * to the latest information it has. If so, the agent will follow this path, and
	 * else the tactic is not enabled.
     * @return
     */
    public static Tactic tryToUnstuck() {
    	Tactic unstuck = action("Trying to unstuck")
    			.do1((BeliefState belief) -> {
    				System.out.println("#### STUCK, probably cannot get past a turn-corner: @" 
    			           + belief.worldmodel.position
    			           + ", current way-point: " 
    			           + belief.getNextWayPoint()) ;
    	    		var unstuckPosition = unstuck(belief) ;
    	    		if (unstuckPosition != null) {
    	    			System.out.println("#### forcing a move past the corner...to " + unstuckPosition) ;        		
    	    			//belief.mentalMap.insertNewWayPoint(unstuckPosition);
    	    			belief.worldmodel.moveToward(belief.env(),unstuckPosition) ;
    	    		}
    	    		else {
    	    			// else .... for now do nothing :|
    	        		System.out.println("#### unfortunately cannot find an unstuck solution...") ; 
    	    		}	       			
    	    	    belief.clearStuckTrackingInfo();
    				return belief ;
    			})
    			.on_((BeliefState belief) -> {
    				//System.out.println(">>> stuck: " + belief.isStuck() + ", goal loc: " + belief.getGoalLocation()) ;
    				return belief.getGoalLocation() != null &&  belief.isStuck() ;
    			})
    			.lift() ;	
    	return unstuck ;
    }
    
    /**
     * If the agent gets stuck in an bending corner (because the navigation algorithm does not
     * take agent's dimension into account... :| ), this method tries to find a position close 
     * to the agent, which can unstuck agent. The agent can travel to this new position, which
     * will move it past the stucking corner.
     * 
     * If such a position can be found, it is returned. Else null is returned.
     * 
     * Note that the method simply checks if there is a navigation polygon that contains this
     * unstuck position. It doesn't check if this position is actually reachable from the agent's
     * current position.
     */
    public static Vec3 unstuck(BeliefState belief) {
    	var unstuck_distance = BeliefState.UNIT_DISTANCE * 0.5 ;
    	var agent_current_direction = Vec3.sub(belief.getNextWayPoint(), belief.worldmodel.position) ;
    	
    	var x_orientation = Math.signum(agent_current_direction.x) ;  // 1 if the agent is facing eastly, and -1 if westly
    	var z_orientation = Math.signum(agent_current_direction.z) ;  // 1 if the agent is facing northly, and -1 if southly
    	// System.out.println("#### calling unstuck()") ;
    	// try E/W unstuck first:
    	if (x_orientation != 0) {
    		var p = belief.worldmodel.getFloorPosition() ;
    		p.x += unstuck_distance*x_orientation ;
    		if (isPointInNavigableSurface(belief,p)) return p ; 
        	//if (mentalMap.pathFinder.graph.vecToNode(p) != null) return p ;
    	}
    	// try N/S unstuck:
    	if (z_orientation != 0) {
    		var p = belief.worldmodel.getFloorPosition() ;
    		p.z += unstuck_distance*z_orientation ;
        	if (isPointInNavigableSurface(belief,p)) return p ; 
        	//if (mentalMap.pathFinder.graph.vecToNode(p) != null) return p ;	
    	}
    	// can't find an unstuck option...
    	return null ;
    }
    
    private static boolean isPointInNavigableSurface(BeliefState belief, Vec3 p) {
    	List<Face> faces = new LinkedList<>() ;
    	faces.addAll(belief.pathfinder.faces) ;
    	belief.pathfinder.vertices.get(0) ;
    	faces.sort((f1,f2) -> 
    	     Float.compare(Vec3.dist(belief.pathfinder.vertices.get(f1.vertices[0]),p), 
    	    		       Vec3.dist(belief.pathfinder.vertices.get(f2.vertices[0]),p))) ;
    	for (Face f : faces) {
    		if (f.distFromPoint(p, belief.pathfinder.vertices) <= 0.02) 
    			return true ;
    	}
    	return false ;
    }
    
    /*
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
    /*
    public static Tactic forcePastExploreBlindCorner() {
    	return action("Trying to force the agent to get past an explore-blind-corner")
    		   . do1((BeliefState belief) -> { 
    			    var unstuckPosition = belief.unstuck() ;
    			    System.out.println("#### invoking forcePastExploreBlindCorner") ;
    			    System.out.println("#### agent velocity " + belief.derived_lastNonZeroXZVelocity()) ;
    			    System.out.println("#### unstuck option " + unstuckPosition) ;  
    			    LabWorldModel o ;
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
    */
    
    /**
     * Send an interact command if the agent is close enough.
     * @param objectID The id of the in-game entity to interact with
     * @return A tactic in which the agent will interact with the object
     */
    public static Tactic interact(String objectID) {
        Tactic interact = action("Interact")
               . do2((BeliefState belief) -> (WorldEntity e) -> {
                	  var obs = belief.worldmodel.interact(belief.env(), LabWorldModel.INTERACT, e)  ;
                	  // force update to worldmodel:
                	  belief.mergeNewObservationIntoWOM(obs);
                      return belief;
                    })
               . on((BeliefState belief) -> {
                	var e = belief.worldmodel.getElement(objectID) ;
                	if (e==null) return null ;
                	if (belief.worldmodel.canInteract(LabWorldModel.INTERACT, e)) {
                		return e ;
                	}
                	return null ;
                    })
               . lift();
        return interact;
    }

    /*
     * This method will return an observe tactic which will do nothing but receive an 
     * observation and update the agent
     * @return A do nothing action
     */
    public static Tactic observe() {
        //this is a wait action which will allow the agent to retrieve an observation
        Tactic observe = action("Observe")
                .do1((BeliefState belief) -> {
                	var obs = belief.worldmodel.observe(belief.env());
                	// force wom update:
                	belief.mergeNewObservationIntoWOM(obs) ;
                    return belief;
                }).lift();
        return observe;
    }

/*
    public static Tactic observeOnce() {
        //this is a wait action which will allow the agent to retrieve an observation
        Tactic observe = action("Observe once")
                .do1((BeliefState belief) -> {
                	LabWorldModel o = belief.env().observe(belief.id);
                    belief.updateBelief(o);
                    return belief;
                }).on((BeliefState b) -> !b.worldmodel.didNothingPreviousGameTurn).lift();
        return observe;
    }
    */

    /**
     * This tactic will allow an agent to observe, and share its observation to other agents.
     */
    public static Tactic shareObservation(String id){
        return action("Share map")
                . do1((BeliefState belief)-> {
                	var obs = belief.worldmodel.observe(belief.env());
                	// force wom update:
                	belief.mergeNewObservationIntoWOM(obs) ;         	
                    Acknowledgement a = belief.messenger().send(id,0, Message.MsgCastType.BROADCAST, "","ObservationSharing",obs) ;
                    return belief;
                }).lift();
    }

    /**
     * This tactic cause the agent to receive an memory share if one is available and make an observation
     * @return The tactic which will receive the memory share
     */
    public static Tactic receiveObservationShare(){
        return action("Receive map sharing")
                . do1((BeliefState belief)-> {
                	//get the  messages
                	Message m = belief.messenger().retrieve(M -> M.getMsgName().equals("ObservationSharing")) ;
                    while(m != null){
                        //apply the memory share
                    	var obs = (LabWorldModel) m.getArgs()[0] ;
                    	if (obs.timestamp >= belief.worldmodel.timestamp) {
                    		belief.worldmodel.mergeNewObservation(obs) ;
                    	}
                    	else {
                    		belief.worldmodel.mergeOldwObservationIntoWOM(obs) ;
                    	}
                    	belief.pathfinder.markAsSeen(obs.visibleNavigationNodes);
                        m = belief.messenger().retrieve(M -> M.getMsgName().equals("ObservationSharing")) ;
                    }
                    //do an observation
                    //LabWorldModel o = belief.env().observe(belief.id);
                    //belief.updateBelief(o);
                    return belief;
                })
                .on_((BeliefState S) -> S.messenger().has(M -> M.getMsgName().equals("ObservationSharing")))//check if there is a memory share available
                .lift() ;
    }


    public static Tactic sendPing(String idFrom, String idTo){
        return action("Send ping")
                . do1((BeliefState belief)-> {
                    //do an observation
                	//LabWorldModel o = belief.env().observe(belief.id);
                    
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
                    //LabWorldModel o = belief.env().observe(belief.id);
                    
                    //return whether we have received an observation yes or no
                    return belief;
                })
                .lift() ;
    }

    /**
     * This method will construct a tactic in which the agent will "explore" the world.
     * The tactic will locate the nearest reachable navigation node which the agent
     * has not discovered yet, and drive the agent to go there.
     */
    public static Tactic explore() {
    	
    	var memo = new MiniMemory("S0") ;
    	
    	var selectExplorationTarget = action("Explore: setting next exploration target")
                . do2((BeliefState belief) -> (Pair<Vec3,List<Vec3>> q) -> {
                	 belief.applyPath(belief.worldmodel.timestamp,q.fst, q.snd);
                	 belief.worldmodel.moveToward(belief.env(),belief.getNextWayPoint());
                     return belief ;
                  })
    			. on((BeliefState belief) -> {
    				 if(!memo.stateIs("S0")) return null ;
                     
    				 float distToFaceThreshold = 0.02f ;
    				 
                     //get the location of the closest unexplored node
    				 var explorationCandidates = belief.pathfinder.explore(belief.worldmodel.getFloorPosition(),0.05f) ;
    				 if (explorationCandidates.isEmpty()) {
    					memo.moveState("exhausted") ;
                        System.out.println("### no new and reachable area found; agent is @" + belief.worldmodel.position) ;
                        return null ;
    				 }
    				 Vec3 explorationTarget = belief.pathfinder.vertices.get(explorationCandidates.get(0)) ;
    	    				 
    				 var path = belief.findPathTo(explorationTarget,true) ;
    				 // remove the last element because it would be in the list 2x:
    				 path.remove(path.size() - 1) ;
                     System.out.println("### setting a new exploration target: " + explorationTarget) ;
                     memo.memorized.clear();
                     memo.memorize(explorationTarget);
                     memo.moveState("inTransit") ;
                     return new Pair(explorationTarget, path);//return the path finding information
                 })
               . lift(); 
    	
    	var exploreInTransit = rawNavigateTo_("Explore: traveling to current exploration target",null)
    		   . on((BeliefState belief) -> {
   				     if(!memo.stateIs("inTransit")) return null ;
                     Vec3 exploration_target = (Vec3) memo.memorized.get(0) ;
                     // note that exploration_target won't be null because we are in the state
                     // in-Transit
                     Vec3 agentLocation = belief.worldmodel.getFloorPosition() ;
                     Vec3 currentDestination = belief.getGoalLocation() ;
                     if (Vec3.dist(agentLocation,exploration_target) <= 0.3 // current exploration target is reached
                         || currentDestination==null 
                         || Vec3.dist(currentDestination,exploration_target) > 0.3) {
                    	 // in all these cases we need to give the control back to selectExplorationTarget
                    	 // to select a new exploration target.
                    	 // This is done by moving back the exploration state
                    	 // to S0.
                    	 memo.moveState("S0");
                     }
                     // System.out.println(">>> explore in-transit: " + memo.stateIs("inTransit")) ;
                     // System.out.println(">>> exploration target: " + exploration_target) ;
                     // We should not need to re-calculate the path. If we are "inTransit" the path is
                     // already in the agent's memory
                     // return new Tuple(g, belief.findPathTo(g));
                     return new Pair(exploration_target,null);
                })
              . lift(); 
   	        
        return FIRSTof(
        		 forceReplanPath(), 
				 tryToUnstuck(),
				 selectExplorationTarget,
				 exploreInTransit) ;
    }

}
