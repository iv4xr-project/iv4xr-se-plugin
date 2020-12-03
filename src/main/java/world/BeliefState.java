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
import nl.uu.cs.aplib.utils.Pair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Stores agent knowledge of the agent itself, the entities it has observed, what parts of the
 * world map have been explored and its current movement goals.
 */
public class BeliefState extends State {

	/**
	 * Threshold value of the distance between a point to a face to determine when
	 * the point is "on" the face, or at least close enough to the face.
	 */
	public static final float DIST_TO_FACE_THRESHOLD = 0.2f ;

	/**
	 * Distance between a location to the currently memorized goal location; if it
	 * is further, pathfinder is triggered to calculate a new path.
	 */
	public static final float DIST_TO_MEMORIZED_GOALLOCATION_SOFT_REPATH_THRESHOLD  = 0.05f ;

	/**
	 * When the distance of the agent to the current waypoint is less than this, the
	 * waypoint is considered achieved, and we advance to next waypoint.
	 */
	public static final float DIST_TO_WAYPOINT_UPDATE_THRESHOLD = 0.5f ;

	/**
	 * All navigation vertices within this distance form the agent will be automatically
	 * added as "seen" (since LR seem not to include them all...bug?).
	 */
	public static final float AUTOVIEW_HACK_DIST = 0.4f ;

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

    public float viewDistance = 10f ;

    /**
     * Since getting to some location may take multiple cycle, and we want to avoid invoking the
     * pathfinder at every cycle, the agent will memorize what the target location it is trying
     * to reach (which we will call "goal-location") and along with it the path leading to it, as
     * returned by the pathfinder. The agent basically only needs to follow this path. We will
     * additionally also need to keep track of where the agent is in this path.
     *
     * Elements of the memorized path will be called "way-points". So, to reach the goal-location,
     * the agent should travel from one way-point to the next one. The field currentWayPoint
     * keeps track of which way-point is the one that the agent currently should travel to.
     */
    private Vec3 memorizedGoalLocation;
    private List<Vec3> memorizedPath;
    private long goalLocationTimeStamp = -1 ;
    /**
     * Pointing the current way-point in the memorized-path, that the agent currently is
     * set to travel to.
     */
    private int currentWayPoint_ = -1;

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
    	return findPathTo(q,true).snd ;
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
	 *
	 * If no re-calculation is needed the method returns the pair(q',null), where
	 * q' is the currenly memorized goal-location.
	 *
	 * If pathfinding is invoked, and results in a path, the pair (q,path) is returned.
	 * If no path can be found, null will be returned.
	 */
    public Pair<Vec3,List<Vec3>> findPathTo(Vec3 q, boolean forceIt) {
    	if (!forceIt && memorizedGoalLocation!=null) {
    		if (Vec3.dist(q,memorizedGoalLocation) <= DIST_TO_MEMORIZED_GOALLOCATION_SOFT_REPATH_THRESHOLD)
    			return new Pair(q,null) ;
    	}
    	// else we invoke the pathfinder to calculate a path:
    	// be careful with the threshold 0.05..
    	var abstractpath = pathfinder.findPath(worldmodel.getFloorPosition(),q,BeliefState.DIST_TO_FACE_THRESHOLD) ;
    	if (abstractpath == null) return null ;
    	List<Vec3> path = abstractpath.stream().map(v -> pathfinder.vertices.get(v)).collect(Collectors.toList()) ;
    	// add the destination path too:
    	path.add(q) ;
    	return new Pair(q,path) ;
    }



    /**
     * True if the agent is "stuck" with respect to the given position q. "Stuck" is
     * defined as follows. Let p0,p1.p3 be the THREE most recent (and registered)
     * positions of the agent, with p0 being the oldest. It is stuck if the distance
     * between p0 and p1 and p0 and p3 are at most 0.05, and the distance between p0 and
     * q is larger than 1.0.
     */
    public boolean isStuck() {
    	//System.out.println("====== checkingh stcuk") ;
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
    			    .filter(d -> Vec3.dist(worldmodel.position,d.position) <= 2.0)
    			    .collect(Collectors.toList());
    }

    /**
     * Set the given destination as the agent current space-navigation destination. The
     * given path is a pre-computed path to get to this destination.
     */
    public void applyPath(long timestamp, Vec3 destination, List<Vec3> path){
    	memorizedGoalLocation = destination;
        memorizedPath = path;
        goalLocationTimeStamp = timestamp ;
        currentWayPoint_ = 0 ;
    }

    /**
     * Get the 3D location which the agent memorized as its current 3D navigation
     * goal/destination.
     */
    public Vec3 getGoalLocation() {
    	if (memorizedGoalLocation == null) return null ;
        return new Vec3(memorizedGoalLocation.x, memorizedGoalLocation.y, memorizedGoalLocation.z)    ;
    }

    /**
     * Return the time when the current 3D location navigation target/destination (and the
     * path to it) was set.
     */
    public long getGoalLocationTimestamp() {
    	return goalLocationTimeStamp ;
    }

    public List<Vec3> getMemorizedPath() {
    	return memorizedPath;
    }
    /**
     * Clear (setting to null) the memorized 3D destination location and the memorized path to it.
     */
    public void clearGoalLocation() {
    	memorizedGoalLocation = null ;
    	memorizedPath = null ;
    	goalLocationTimeStamp = -1 ;
    	currentWayPoint_ = -1 ;
    }

    /**
     * Update the current wayPoint. If it is reached, it should be moved to the next one,
     * unless if it is the last one, then it will not be advanced.
     *
     * This method will not clear the memorized way-point nor the path leading to it. The
     * agent must explicitly does so itself, based on its own consideration.
     */
    private void updateCurrentWayPoint() {
    	if (memorizedGoalLocation == null) return ;
    	var delta = Vec3.dist(worldmodel.getFloorPosition(), memorizedPath.get(currentWayPoint_)) ;
    	//System.out.println(">>> path" + memorizedPath) ;
    	//System.out.println(">>> currentwaypoint " + currentWayPoint + ", delta: " + delta) ;

    	// be careful with this threshold... seems to be somewhat sensitive; the mesh generated by
    	// LR seem not to take the width of the character into account, which can cause problem
    	// as to trying to move an agent to a position it cannot reach due to its width
    	if (delta <= DIST_TO_WAYPOINT_UPDATE_THRESHOLD) {
    		// the current way-point is reached, advance to the next way-point, unless it
    		// is the last one.
    		// the agent must clear or set another goal-location explicitly by itself.
            if (currentWayPoint_ < memorizedPath.size() - 1) {
        		currentWayPoint_++ ;
            }
        }
    }

    /**
     * Insert as the new current way-point, and shift the old current and the ones after it
     * to the right in the memorized path.
     */
    public void insertNewWayPoint(Vec3 p) {
    	if (currentWayPoint_ < memorizedPath.size()) {
        	memorizedPath.add(currentWayPoint_,p) ;
    	}
     }

    /**
     * When the agent has been assigned a destination (a 3D location) to travel to, along with
     * the path to get there, it can then follow this path. Each position in the path is
     * called a way-point. It can move thus from a way-point to the next one. This method
     * returns what is the current way-point the agent is trying to go to.
     */
    public Vec3 getCurrentWayPoint() {
        return memorizedPath.get(Math.min(currentWayPoint_, memorizedPath.size()-1)) ;
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
        // updating recent positions tracking (to detect stuck) here, rater than in mergeNewObservationIntoWOM,
        // because the latter is also invoked directly by some tactic (Interact) that do not/should not update
        // positions
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

    static List<Integer> arrayToList(int[] a) {
    	List<Integer> s = new LinkedList<>() ;
    	for (int i : a) s.add(i) ;
    	return s ;
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
    	// HACK.. adding nearby vertices as seen:
    	int NumOfvertices = pathfinder.vertices.size() ;
    	if (worldmodel.position != null) {
    		for (int v=0; v<NumOfvertices; v++) {
        		if (Vec3.dist(worldmodel.getFloorPosition(), pathfinder.vertices.get(v)) <= AUTOVIEW_HACK_DIST) {
        			 pathfinder.seenVertices.set(v,true) ;
        		}
        	}
    	}


    	// System.out.println("### seeing these nodes: "  +  arrayToList(observation.visibleNavigationNodes)) ;

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

        // update the tracking of memorized path to follow:
        updateCurrentWayPoint();
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
        pathfinder = new SurfaceNavGraph(e_.worldNavigableMesh,0.5f) ; // area-size threshold 0.5 
        return this;
    }

}
