/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import environments.LabRecruitsConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.extensions.pathfinding.SurfaceNavGraph;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Obstacle;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.agents.State;
import nl.uu.cs.aplib.mainConcepts.Environment;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Stores agent knowledge of the agent itself, the entities it has observed, what parts of the
 * world map have been explored and its current movement goals.
 */
public class BeliefState extends State {

	/**
	 * The id of the agent that owns this belief.
	 */
    public String id;
    
    /**
     * To keep track entities the agent has knowledge about (not necessarily up to date knowledge).
     */
    public LabWorldModel worldmodel  = new LabWorldModel() ;
    
    /**
     * A 3D-surface pathfinder to guide agent to navigate over the game world.
     */
    public SurfaceNavGraph pathfinder ;
    
    public Boolean receivedPing = false;//store whether the agent has an unhandled ping
    
    public static final float UNIT_DISTANCE = 1f ;

    public float viewDistance = 10f ;
    
    /**
     * Since getting to some location may take multiple cycle, and we want to avoid invoking the
     * pathfinder at every cycle, the agent will memorize what the target location it is trying
     * to reach. This is called "goal-location", and along with it the path leading to it, as 
     * returned by the pathfinder. The agent basically only needs to follow this path. We will
     * additionally also need to keep track of where the agent is in this path.
     */
    private Vec3 memorizedGoalLocation;
    private List<Vec3> memorizedPath;
    private long pathTimestamp = -1 ;
    /**
     * Pointing to where the agent is, in the memorizedPath.
     */
    private int currentWayPoint = -1;
    
    List<Vec3> recentPositions = new LinkedList<>() ; 

    public BeliefState() { }

    public Collection<WorldEntity> knownEntities() { return worldmodel.elements.values(); }
    
    
    // lexicographically comparing e1 and e2 based on its age and distance:
    private int compareAgeDist(WorldEntity e1, WorldEntity e2) {
    	var c1 = Long.compare(age(e1),age(e2)) ;
    	if (c1 != 0) return c1 ;
    	return Double.compare(distanceTo(e1),distanceTo(e2)) ;
    }
    
    /**
     * Return all the buttons in the agent's belief.
     */
    public List<WorldEntity> knownButtons() { 
    	return knownEntities().stream()    	
    			. filter(e -> e.type.equals("Switch"))
    			. collect(Collectors.toList()) ;		
    }

    /**
     * Return all the buttons in the agent's belief, sorted in ascending age
     * (so, from the most recently updated to the oldest) and distance.
     */
    public List<WorldEntity> knownButtons_sortedByAgeAndDistance() { 
    	var buttons = knownButtons() ;
    	buttons.sort((b1,b2) -> compareAgeDist(b1,b2)) ;
    	return buttons ;
    }
    
    /**
     * Return all the doors in the agent's belief.
     */
    public List<WorldEntity> knownDoors() { 
    	return knownEntities().stream()    	
    			. filter(e -> e.type.equals("Door"))
    			. collect(Collectors.toList()) ;
    }

    public List<WorldEntity> knownDoors_sortedByAgeAndDistance() { 
    	var doors = knownDoors() ;
    	doors.sort((b1,b2) -> compareAgeDist(b1,b2)) ;
    	return doors ;
    }
    
    /**
     * Return how much time in the past, since the entity's last update.
     */
    public Long age(WorldEntity e) {
    	if (e==null) return null ;
    	return this.worldmodel.timestamp - e.timestamp ;
    }
    
    public Long age(String id) { return age(worldmodel.getElement(id)) ; }
    
    /**
     * Like derivedLastVelocity(), but if it tries to get the last known
     * and non-zero XZ-velocity that can be inferred from tracked positions.
     * Note: the agent track the last 4 positions, as far as these are
     * available. An unstuck routine may clear these tracked positions.
     */
    public Vec3 derived_lastNonZeroXZVelocity() {
    	int N = recentPositions.size() ;
    	if (N<2) return null ;
    	//System.out.println("#### #recentPositions=" + N) ;
    	for(int k=N; k>1; k--) {
    		Vec3 q = Vec3.sub(recentPositions.get(k-1), recentPositions.get(k-2)) ;
        	if (q.x != 0 || q.z != 0) return q ;
    	}
    	return null ;
    }
    
	/**
	 * Well ... the "velocity" field turns out to be always zero. So for now, this
	 * method will calculate "derived" last velocity, based on the difference
	 * between the last two tracked positions of the agent. Note that this is an
	 * approximation, and moreover is not always available (e.g. if we just clear
	 * the tracking).
	 */
    public Vec3 derivedVelocity() {
    	int N = recentPositions.size() ;
    	if (N<2) return null ;
    	Vec3 q = Vec3.sub(recentPositions.get(N-1),recentPositions.get(N-2)) ;
    	return q ;
    }

    /**
     * Inspect the given predicate on the in-game entity with the given id. Note that
     * if the entity if not in the agent's belief (so, a query on it on the belief would
     * return null) this method will return a false.
     */
    public boolean evaluateEntity(String id, Predicate<WorldEntity> predicate) {
    	WorldEntity e  = worldmodel.getElement(id) ;
    	if (e==null) return false ;
        return predicate.test(e);
    }
    
    /***
     * Check if a button is active (in its "on" state).
     */
    public boolean isOn(WorldEntity button) {
    	return button!= null && button.getBooleanProperty("isOn") ;
    }

    public boolean isOn(String id) { return isOn(worldmodel.getElement(id)) ; }
    
    /**
     * Check if a door is active/open.
     */
    public boolean isOpen(WorldEntity door) {
    	return door != null && door.getBooleanProperty("isOpen") ;
    }

    public boolean isOpen(String id) { return isOpen(worldmodel.getElement(id)) ; }
    
	/**
	 * Calculate the straight line distance from the agent to an entity, without
	 * regard if the entity is actually reachable.
	 */
    public double distanceTo(WorldEntity e) {
    	if (e==null) return Double.POSITIVE_INFINITY ;
    	return Vec3.dist(worldmodel.position, e.position) ;
    }
    
    public double distanceTo(String id) { return distanceTo(worldmodel.getElement(id)) ; }
    
	/**
	 * Check if the agent belief there is a path from its current location to the
	 * entity e. If so, a path is returned, and else null. Do note that
	 * path-checking can be expensive.
	 */
    public List<Vec3> canReach(WorldEntity e) {
    	if (e==null) return null ;
    	return canReach(((LabEntity) e).getFloorPosition()) ;
    }
    
	/**
	 * Check if the agent believes that given position is reachable. That is, if a
	 * navigation route to the position, through its nav-graph, exists. If this is
	 * so, the route/path is returned; else null. Be aware that this might be an expensive
	 * query as it trigger a fresh path finding calculation.
	 */    
    public List<Vec3> canReach(Vec3 q) {
    	return findPathTo(q,true) ;
    }
    
	/**
	 * Check if the agent believes that given position is reachable. That is, if a
	 * navigation route to the position, through its nav-graph, exists. If this is
	 * so, the route/path is returned. Be aware that this might be an expensive
	 * query as it trigger a fresh path finding calculation.
	 */ 
    public List<Vec3> canReach(String id) { return canReach(worldmodel.getElement(id)) ; }
    
	/**
	 * Invoke the pathfinder to calculate a path from the agent current location
	 * to the given location q.
	 * 
	 * If the flag "force" is set to true, the pathfinder will really be invoked.
	 * 
	 * If it is set to false, the method first compares q with the goal-location
	 * it memorizes. If they are very close, the method assumes the destination
	 * is still the same, and that the agent is following the memorized path to it.
	 * In this case, it won't recalculate the path, but simply return the memorized
	 * path.
	 */
    public List<Vec3> findPathTo(Vec3 q, boolean forceIt) {
    	if (!forceIt && memorizedGoalLocation!=null) {
    		if (Vec3.dist(q,memorizedGoalLocation) <= 0.05) return memorizedPath ;
    	}
    	// else we invoke the pathfinder to calculate a path:
    	var abstractpath = pathfinder.findPath(worldmodel.getFloorPosition(),q,0.05f) ;
    	if (abstractpath == null) return null ;
    	List<Vec3> path = abstractpath.stream().map(v -> pathfinder.vertices.get(v)).collect(Collectors.toList()) ;
    	// add the destination path too:
    	path.add(q) ;
    	return path ;
    }
    

    
    /**
     * True if the agent is "stuck" with respect to the given position q. "Stuck" is
     * defined as follows. Let p0,p1.p3 be the THREE most recent (and registered)
     * positions of the agent, with p0 being the oldest. It is stuck if the distance
     * between p0 and p1 and p0 and p3 are at most 0.05, and the distance between p0 and
     * q is larger than 1.0.
     */
    public boolean isStuck() {
    	int N = recentPositions.size() ;
    	if(N<3) return false ;
    	Vec3 p0 = recentPositions.get(N-3) ;
    	return Vec3.dist(p0,recentPositions.get(N-2)) <= 0.05
    		   &&  Vec3.dist(p0,recentPositions.get(N-1)) <= 0.05 ;
         //    && p0.distance(q) > 1.0 ;   should first translate p0 to the on-floor coordinate!
    }
    
    /**
     * Clear the tracking of the agent's most recent positions. This tracking is done to detect
     * if the agent gets stuck.
     */
    public void clearStuckTrackingInfo() {
    	recentPositions.clear();
    }
    
	/**
	 * Return all close-by doors around the agent. Else an empty list is returned.
	 * "Close" is being defined as within a distance of 1.0.
	 */
    public List<WorldEntity> closebyDoor() {
    	var doors = knownDoors() ;
    	return doors.stream()
    			    .filter(d -> Vec3.dist(worldmodel.position,d.position) <= 1.0)
    			    .collect(Collectors.toList());
    }
  
    /**
     * Set the given destination as the agent current space-navigation destination. The
     * given path is a pre-computed path to get to this destination.
     */
    public void applyPath(long timestamp, Vec3 destination, List<Vec3> path){
    	memorizedGoalLocation = destination;
        memorizedPath = path;
        pathTimestamp = timestamp ;
        currentWayPoint = 0 ;
    }
    
    /**
     * Get the 3D location which the agent memorized as its current 3D navigation 
     * goal/destination.
     */
    public Vec3 getGoalLocation() {
        return new Vec3(memorizedGoalLocation.x, memorizedGoalLocation.y, memorizedGoalLocation.z)    ;
    }

    /** 
     * Clear (setting to null) the memorized 3D destination location and the memorized path to it.
     */
    public void clearGoalLocation() {
    	memorizedGoalLocation = null ;
    	memorizedPath = null ;
    	pathTimestamp = -1 ;
    	currentWayPoint = -1 ;
    }
    
    /**
     * Update the current wayPoint if a new wayPoint has been reached
     *
     * @param position: The position of the agent
     * @return Whether the waypoint was updated or not
     */
    private boolean updateCurrentWayPoint(Vec3 position) {
    	if(currentWayPoint >= memorizedPath.size()) {
            // goalLocation = null;//disable the goal to signal that the goal is reached
            return false;
        }
        //if the agent is close to the current wayPoint update to follow the next wayPoint
        if (Vec3.dist(position, memorizedPath.get(currentWayPoint)) <= 0.2) {
            currentWayPoint++ ;
            return true ;
        }
        return false ;
    }
    
    /**
     * Insert as the new current way-point, and shift the old current and the ones after it
     * to the right in the memorized path.
     */
    public void insertNewWayPoint(Vec3 p) {
    	if (currentWayPoint < memorizedPath.size()) {
        	memorizedPath.add(currentWayPoint,p) ;    		
    	}
     }
    
    /**
     * When the agent has been assigned a destination (a 3D location) to travel to, along with
     * the path to get there, it can then follow this path. Each position in the path is
     * called a way-point. It can move thus from a way-point to the next one. This method
     * returns what is the current way-point the agent is trying to go to.
     */
    public Vec3 getNextWayPoint() {
        return memorizedPath.get(Math.min(currentWayPoint, memorizedPath.size()-1)) ;
    }
	
    /**
     * This method will be called automatically by the agent when the agent.update()
     * method is called.
     */
    @Override
    public void updateState() {
        super.updateState();
        var observation = this.env().observe(id) ;
        mergeNewObservationIntoWOM(observation) ;
        recentPositions.add(new Vec3(worldmodel.position.x, worldmodel.position.y, worldmodel.position.z)) ;
        if (recentPositions.size()>4) recentPositions.remove(0) ;
    }
    
    /**
     * Check if a game entity identified by is is already registered as an obstacle in the
     * navigation graph. If it is, the entity will be returned (wrapped as an instance of
     * the class Obstacle). Else null is returned.
     */
    private Obstacle findThisObstacleInNavGraph(String id) {
    	for (Obstacle o : pathfinder.obstacles) {
    		LabEntity e = (LabEntity) o.obstacle ;
    		if (e.id.equals(id)) return o ;
    	}
    	return null ;
    }
    
    
    /**
     * Update the agent's belief state with new information from the environment.
     *
     * @param observation: The observation used to update the belief state.
     */
    public void mergeNewObservationIntoWOM(LabWorldModel observation) {
    	
    	// if there is no observation given (e.g. because the socket-connection times out)
    	// then obviously there is no information to merge either.
    	// We then simply return.
    	if (observation == null) return ;
    	
    	// update the navigation graph with nodes seen in the new observation
    	pathfinder.markAsSeen(observation.visibleNavigationNodes) ;
    	// update the tracking of memorized path to follow:
    	updateCurrentWayPoint(worldmodel.getFloorPosition());
    	
    	// add newly discovered Obstacle-like entities, or change their states if they are like doors
    	// which can be open or close:
    	var impactEntities = worldmodel.mergeNewObservation(observation) ;
    	// recalculating navigation nodes that become blocked or unblocked:
        boolean refreshNeeded = false ;
        for (var e : impactEntities) {
        	if (e.type.equals(LabEntity.DOOR) || e.type.equals(LabEntity.COLORSCREEN) || e.type.equals(LabEntity.GOAL)) {
        		Obstacle o = findThisObstacleInNavGraph(e.id) ;
	       		if (o==null) {
	       			 // e has not been added to the navgraph, so we add it, and retrieve its
	       			 // Obstacle-wrapper:
	       			 pathfinder.addObstacle((LabEntity) e); 
	       			 int N = pathfinder.obstacles.size();
	       			 o = pathfinder.obstacles.get(N-1) ;	
	       			 if (! e.type.equals(LabEntity.DOOR)) {
	       				 // unless it is a door, the obstacle is always blocking:
	       				 o.isBlocking = true ;
	       			 }
	       		}
	       		// if it is a door, toggle the blocking state of o to reflect the blocking state of e:
	       		if (e.type.equals(LabEntity.DOOR)) {
	       		   o.isBlocking = worldmodel.isBlocking(e) ;
	       		}
        	}
        }
    }

    /*
     * Test if the agent is within the interaction bounds of an object.
     * @param id: the object to look for.
     * @return whether the object is within reach of the agent.
     */
    public boolean canInteract(String id) {
    	var e = worldmodel.getElement(id) ;
    	if (e==null) return false ;
   	    return worldmodel.canInteract(LabWorldModel.INTERACT, e) ;
    }

    /**
     * Copy the view-distance from the given LR configuration into this Belief.
     */
    public void setViewDistance(LabRecruitsConfig config) {
    	viewDistance = config.view_distance ;
    }
    
    /**
     * True if the given position q is within the viewing range of the
     * agent. This is defined by the field viewDistance, whose default is 10.
     */
    public boolean withinViewRange(Vec3 q){
    	return worldmodel.position != null 
    			&& Vec3.sub(worldmodel.getFloorPosition(),q).lengthSq() < viewDistance * viewDistance;
    }
    
    /**
     * True if the entity is "within viewing range" of the agent. This is defined by the field viewDistance,
     * whose default is 10.
     */
    public boolean withinViewRange(WorldEntity e){
    	return e != null && withinViewRange(((LabEntity) e).getFloorPosition());
    }
    
    public boolean withinViewRange(String id){ return withinViewRange(worldmodel.getElement(id)) ; }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String sep = ", ";
        sb.append("BeliefState\n [ ");
        for (var e : worldmodel.elements.values()) {
            sb.append("\n\t");
            sb.append(e.toString());
        }
        sb.append("\n]");
        return sb.toString();
    }

    @Override
    public LabRecruitsEnvironment env() {
        return (LabRecruitsEnvironment) super.env();
    }

    /**
     * Link an environment to this Belief. It must be an instance of LabRecruitsEnvironment.
     * This will also create an instance of SurafaceNavGraph from the navigation-mesh of the 
     * loaded LR-level stored in the env.
     */
    @Override
    public BeliefState setEnvironment(Environment e) {
    	super.setEnvironment(e) ;
        if (! (e instanceof LabRecruitsEnvironment)) throw new IllegalArgumentException("Expecting an instance of LabRecruitsEnvironment") ;
        LabRecruitsEnvironment e_ = (LabRecruitsEnvironment) e ;        
        pathfinder = new SurfaceNavGraph(e_.worldNavigableMesh) ;
        return this;
    }

}
