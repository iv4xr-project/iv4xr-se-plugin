/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import communication.agent.AgentCommandType;
import environments.LabRecruitsEnvironment;
import helperclasses.Intersections.EntityNodeIntersection;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import nl.uu.cs.aplib.agents.StateWithMessenger;
import nl.uu.cs.aplib.mainConcepts.Environment;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Stores agent knowledge of the agent itself, the entities it has observed, what parts of the
 * world map have been explored and its current movement goals.
 */
public class BeliefState extends StateWithMessenger {

    public String id;
    public Vec3 position;
    public Vec3 velocity;
    /**
     * In-game entities that the agent is aware of. Represented as a mapping from
     * their ids.
     */
    private HashMap<String, Entity> entities = new HashMap<>();
    //private HashMap<String, DynamicEntity> dynamicEntities = new HashMap<>();
    //private HashMap<String, InteractiveEntity> interactiveEntities = new HashMap<>();
    public MentalMap mentalMap;

    public int lastUpdated = -1;
    public boolean didNothingPreviousTurn;

    
    public Boolean receivedPing = false;//store whether the agent has an unhandled ping

    
    /**
     * keep track of nodes which are blocked and can not be used for pathfinding
     */
    public HashSet<Integer> blockedNodes = new HashSet<>();
    private HashMap<String, Integer[]> nodesBlockedByEntity = new HashMap<>();
    
    List<Vec3> recentPositions = new LinkedList<>() ;

    public BeliefState() { }

    public Collection<Entity> knownEntities() { return entities.values(); }
    public Collection<DynamicEntity> knownDynamicEntities() { 
    	return entities.values().stream()
    			. filter(e -> e instanceof DynamicEntity)
    			. map(e -> (DynamicEntity) e)
    			. collect(Collectors.toList()) ;
    }
    
    public Collection<InteractiveEntity> knownInteractiveEntities() { 
    	return entities.values().stream()
    			. filter(e -> e instanceof InteractiveEntity)
    			. map(e -> (InteractiveEntity) e)
    			. collect(Collectors.toList()) ;
    }
    
    public boolean isDoor(Entity e) {
    	return (e != null && e instanceof InteractiveEntity) && e.tag.equals("Door") ;
    }
    
    /**
     * Check if the entity is a button. Currently it is assumed to be a button if ... 
     * its id starts with b or B :|
     */
    public boolean isButton(Entity e) {
    	return (e !=null && e instanceof InteractiveEntity) && (e.id.startsWith("b") || e.id.startsWith("B")) ;
    }
    
    // lexicographically comparing e1 and e2 based on its age and distance:
    private int compareAgeDist(Entity e1, Entity e2) {
    	var c1 = Integer.compare(age(e1),age(e2)) ;
    	if (c1 != 0) return c1 ;
    	return Double.compare(distanceTo(e1),distanceTo(e2)) ;
    }
    
    /**
     * Return all the buttons in the agent's belief.
     */
    public List<InteractiveEntity> knownButtons() { 
    	return knownInteractiveEntities().stream()    	
    			. filter(e -> isButton(e))
    			. collect(Collectors.toList()) ;		
    }

    /**
     * Return all the buttons in the agent's belief, sorted in ascending age
     * (so, from the most recently updated to the oldest) and distance.
     */
    public List<InteractiveEntity> knownButtons_sortedByAgeAndDistance() { 
    	var buttons = knownButtons() ;
    	buttons.sort((b1,b2) -> compareAgeDist(b1,b2)) ;
    	return buttons ;
    }
    
    /**
     * Return all the doors in the agent's belief.
     */
    public List<InteractiveEntity> knownDoors() { 
    	return knownInteractiveEntities().stream()    	
    			. filter(e -> isDoor(e))
    			. collect(Collectors.toList()) ;
    }

    public List<InteractiveEntity> knownDoors_sortedByAgeAndDistance() { 
    	var doors = knownDoors() ;
    	doors.sort((b1,b2) -> compareAgeDist(b1,b2)) ;
    	return doors ;
    }
    
    /**
     * Return how many update rounds in the past, since the entity's last update.
     */
    public Integer age(Entity e) {
    	if (e==null) return null ;
    	return this.lastUpdated - e.lastUpdated ;
    }
    
    public Integer age(String id) { return age(getEntity(id)) ; }
    
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
    		Vec3 q = new Vec3(recentPositions.get(k-1)) ;
        	q.subtract(recentPositions.get(k-2)) ;
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
    	Vec3 q = new Vec3(recentPositions.get(N-1)) ;
    	q.subtract(recentPositions.get(N-2)) ;
    	return q ;
    }

    public Integer[] getNodesBlockedByEntity(String id){
        return nodesBlockedByEntity.getOrDefault(id, new Integer[]{});
    }

    /**
     * True if an entity with the given id exists in the agent's belief.
     */
    public boolean entityExists(String id) {
        return getEntity(id) != null ;
    }
 
    public Entity getEntity(String id) {
        return entities.getOrDefault(id, null);
    }
    public InteractiveEntity getInteractiveEntity(String id){
    	return (InteractiveEntity) getEntity(id) ;
    }
    public DynamicEntity getDynamicEntity(String id){
    	return (DynamicEntity) getEntity(id) ;
    }

    // predicates
    public boolean evaluateEntity(String id, Predicate<Entity> predicate) {
    	Entity e  = getEntity(id) ;
    	if (id==null) return false ;
        return predicate.test(e);
    }
    
    public boolean evaluateInteractiveEntity(String id, Predicate<InteractiveEntity> predicate){
    	return evaluateEntity(id, e -> 
    	e instanceof InteractiveEntity && predicate.test((InteractiveEntity) e)) ;
    }
    
    /***
     * Check if a button is active (in its "on" state).
     */
    public boolean isOn(InteractiveEntity button) {
    	return button!= null && button.isActive ;
    }

    public boolean isOn(String id) { return isOn(getInteractiveEntity(id)) ; }
    
    /**
     * Check if a door is active/open.
     */
    public boolean isOpen(InteractiveEntity door) {
    	return door != null && door.isActive ;
    }

    public boolean isOpen(String id) { return isOpen(getInteractiveEntity(id)) ; }
    
	/**
	 * Calculate the straight line distance from the agent to an entity, without
	 * regard if the entity is actually reachable.
	 */
    public double distanceTo(Entity e) {
    	if (e==null) return Double.POSITIVE_INFINITY ;
    	return position.distance(e.position) ;
    }
    
    public double distanceTo(String id) { return distanceTo(getEntity(id)) ; }
    
	/**
	 * Check if the agent belief there is a path from its current location to the
	 * entity e. If so, a path is returned, and else null. Do note that
	 * path-checking can be expensive.
	 */
    public Vec3[] canReach(Entity e) {
    	if (e==null) return null ;
    	return canReach(e.position) ;
    }
    
	/**
	 * Check if the agent believes that given position is reachable. That is, if a
	 * navigation route to the position, through its nav-graph, exists. If this is
	 * so, the route/path is returned. Be aware that this might be an expensive
	 * query as it trigger a fresh path finding calculation.
	 */    
    public Vec3[] canReach(Vec3 q) {
    	return findPathTo(q) ;
    }
    
	/**
	 * Check if the agent believes that given position is reachable. That is, if a
	 * navigation route to the position, through its nav-graph, exists. If this is
	 * so, the route/path is returned. Be aware that this might be an expensive
	 * query as it trigger a fresh path finding calculation.
	 */ 
    public Vec3[] canReach(String id) { return canReach(getEntity(id)) ; }
    
    
    // add
    private void addEntity(Entity newEntity){
        // set the right tick ... so the new entity will get the most recent timestamp:
        newEntity.lastUpdated = this.lastUpdated;
        // add to the entity list
        entities.put(newEntity.id, newEntity);
    }

	/**
	 * Invoke the mental map to find a path. This triggers a fresh path calculation
	 * to make sure that the latest belief is used.
	 *
	 * @param goal: The position where the agent wants to move to.
	 * @return The path found
	 */
    public Vec3[] findPathTo(Vec3 q) {
        return mentalMap.navigateForce(position, q, blockedNodes);
    }

    /**
     * Invoke the mental map to find a path, unless it already has a path towards it.
     *
     * @param goal: The position where the agent wants to move to.
     * @return The path found or the already stored path if the goal location is equal to the previous goal location
     */
    public Vec3[] cachedFindPathTo(Vec3 q) {
        return mentalMap.navigate(position, q, blockedNodes);
    }
    
    /**
     * This method will make the agent move a certain (small) max distance toward the given position.
     */
    public Observation moveToward(Vec3 q) {
    	var o = env().moveToward(this.id, this.position, q) ;
    	recentPositions.add(o.agentPosition) ;
    	// keep track the last FOUR positions
    	if (recentPositions.size()>4) recentPositions.remove(0) ;
    	return o ;
    }
    
    /**
     * True if the agent is "stuck" with respect to the given position q. "Stuck" is
     * defined as follows. Let p0,p1.p3 be the THREE most recent (and registered)
     * positions of the agent, with p0 being the oldest. It is stuck if the distance
     * between p0 and p1 and p0 and p3 are at most 0.05, and the distance between p0 and
     * q is larger than 1.0.
     */
    public boolean isStuck(Vec3 q) {
    	int N = recentPositions.size() ;
    	if(N<3) return false ;
    	Vec3 p0 = recentPositions.get(N-3) ;
    	return p0.distance(recentPositions.get(N-2)) <= 0.05
    		   &&  p0.distance(recentPositions.get(N-1)) <= 0.05 ;
         //    && p0.distance(q) > 1.0 ;
    }
    
    /**
     * Clear the tracking of the agent's most recent positions. This tracking is done to detect
     * if the agent gets stuck.
     */
    public void clearStuckTrackingInfo() {
    	recentPositions.clear();
    }
    
    /**
     * Check if the agent is located close to a door. If so, the door is returned, and otherwise null.
     * "Close" is being defined as within a distance of 0.6 unit.
     */
    public InteractiveEntity atDoor() {
    	var doors = knownDoors() ;
    	for (var d : doors) {
    		if (position.distance(d.position) <= CLOSE_RANGE) return d ;
    	}
    	return null ;
    }
    
    private static double sign_(double x) {
    	if (x>0) return 1 ;
    	if (x<0) return -1 ;
    	return 0 ;
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
    public Vec3 unstuck() {
    	var unstuck_distance = UNIT_DISTANCE * 0.5 ;
    	// use derived velocity...
    	var velocity_ = derived_lastNonZeroXZVelocity() ;
    	if (velocity_ == null) return null ;
    	var x_orientation = sign_(velocity_.x) ;  // 1 if the agent is facing eastly, and -1 if westly
    	var z_orientation = sign_(velocity_.z) ;  // 1 if the agent is facing northly, and -1 if southly
    	// System.out.println("#### calling unstuck()") ;
    	// try E/W unstuck first:
    	if (x_orientation != 0) {
    		var p = new Vec3(position.x + unstuck_distance*x_orientation, position.y, position.z) ;
    		if (mentalMap.getContainingPolygon(p) != null) return p ; 
        	//if (mentalMap.pathFinder.graph.vecToNode(p) != null) return p ;
    	}
    	// try N/S unstuck:
    	if (z_orientation != 0) {
    		var p = new Vec3(position.x, position.y, position.z + unstuck_distance*z_orientation) ;
        	if (mentalMap.getContainingPolygon(p) != null) return p ; 
        	//if (mentalMap.pathFinder.graph.vecToNode(p) != null) return p ;	
    	}
    	// can't find an unstuck option...
    	return null ;
    }
    

    /**
     * Get the goal location of the agent.
     *
     * @return The current goal location used for the path finding.
     */
    public Vec3 getGoalLocation() {
        return mentalMap.getGoalLocation();
    }

    /**
     * This method will return the next wayPoint for an agent to move to.
     *
     * @return The coordinates of the next way point from the calculated path.
     */
    public Vec3 getNextWayPoint() {
        return mentalMap.getNextWayPoint();
    }

    /**
     * Update the agent's belief state with new information from the environment.
     *
     * @param observation: The observation used to update the belief state.
     */
    public void updateBelief(Observation observation) {

        //check if the observation is not null
        if (observation == null) throw new IllegalArgumentException("Null observation received");

        didNothingPreviousTurn = observation.didNothing;
        position = observation.agentPosition;
        velocity = observation.velocity;
        lastUpdated++;
        
        // check if some interactive entities has changed state; need to check this here before
        // updating their state into this state (below)
        var someInteractivityEntity_hasChangedState = anyInteractiveEntityChanged(observation) ;

        for(var e : observation.entities){
            this.addEntity(e); // handle updates / new entities
            
            // WP note:
            // For each entity, below we calculate which navigation nodes which would be
            // blocked by the entity. However, we should keep in mind that an open door
            // The fragment below call EntityNodeIntersection.getNodesBlockedByInteractiveEntity
            // which in turn decide, based on the entity-type/tag if the entity is blocking.
            // Currently only doors are defined to be blocking. E.g. buttons are not categorized
            // as blocking.  --> this logic is shaky!!
            // 
            // The logic with open doors (which should be unblocking) is implemented
            // as a post-processing in the call to recalculateBlockedNodes()
            // a bit further below.
            
            //if (e instanceof InteractiveEntity && !interactiveEntityExists(e.id)) 
            if (e instanceof InteractiveEntity) {
                    	
            	Integer[] blocked = EntityNodeIntersection.getNodesBlockedByInteractiveEntity((InteractiveEntity) e, mentalMap.pathFinder.navmesh); 
            	//System.out.println("### calculating blocked nodes by entity " + e.id +  ": " + blocked.length) ;
            
                nodesBlockedByEntity.put(e.id,blocked) ;
            }
        }

        //update the seen nodes and position if there exists have a mental map
        if(mentalMap != null) {
            mentalMap.updateKnownVertices(observation.navMeshIndices);
            mentalMap.updateCurrentWayPoint(position);
        }

        //check if we need to recalculate the nodes
        //if(anyInteractiveEntityChanged(observation))
        if (someInteractivityEntity_hasChangedState)	{
        	//System.out.println("### interactive state change detected.!") ;
            //if the blocked nodes needs updating, do so
            recalculateBlockedNodes();
        }
    }

    /**
     * Return the position of the closest unknown node
     * @param startPosition: The position from which to look for unknown neighbours
     * @param targetPosition: The position in which direction the agent wants to explore
     * @return The vec3 coordinate of the closest neighbour or null if no neighbour is available
     */
    public Vec3 getUnknownNeighbourClosestTo(Vec3 startPosition, Vec3 targetPosition) {
        return mentalMap.getUnknownNeighbourClosestTo(startPosition, targetPosition, blockedNodes);
    }

    /**
     * Test if the agent is within the interaction bounds of an object.
     * @param id: the object to look for.
     * @return whether the object is within reach of the agent.
     */
    public boolean canInteractWith(String id) {
    	var e = getInteractiveEntity(id) ;
    	if (e==null) return false ;
        return e.canInteract(position);
    }

    /**
     * update the blocked nodes set according to the nodes blocked by entity list
     */
    private void recalculateBlockedNodes(){
        blockedNodes = new HashSet<>();

        //iterate over all key value pairs
        for(var kv: nodesBlockedByEntity.entrySet()) {
            // decide which entity can be blocking; if so add its blocked nodes.
        	// Currently only closed doors are blocking
        	InteractiveEntity ie_ = getInteractiveEntity(kv.getKey()) ;
        	//System.out.println("xxxx "  + ie_.id + ", tag:" + ie_.tag + ", active: " + ie_.isActive) ;
            
            if(evaluateInteractiveEntity(kv.getKey(), ie -> ie.tag.equals("Door") && !ie.isActive)) {
            	Collections.addAll(blockedNodes, kv.getValue());
            }
        }
    }

    /**
     * Check if any interactive entity has changed status
     * @return A boolean whether a recalculate is needed
     */
    private boolean anyInteractiveEntityChanged(Observation o){
        //loop over the interactive entities
        for (var newEntity: getInteractables(o.entities)) {
            //check if there is an update of an entity
        	var originalEntity = getInteractiveEntity(newEntity.id) ;
        	if (originalEntity == null) return true ;
        	if (originalEntity.isActive != newEntity.isActive) return true ;
        	// FIX
        	// This orginal code is incorrect! If the new entity did not exists in the belief, it will be marked as unchanged:
            //if(evaluateInteractiveEntity(newEntity.id, (InteractiveEntity originalEntity) -> originalEntity.isActive != newEntity.isActive ))
            //    return true;
        }
        return false;
    }

    private Iterable<InteractiveEntity> getInteractables(List<Entity> list){
        return new QArrayList<>(list)
                .where((Entity e) -> e instanceof InteractiveEntity)
                .select((Entity e) -> (InteractiveEntity) e);
    }
    
    public static final float IN_RANGE = 0.4f ;
    public static final float CLOSE_RANGE = 0.6f ;
    public static final float UNIT_DISTANCE = 1f ;

    /**
     * True if the entity time stamp (its last update) is the same as the agent's.
     */
    public boolean entityIsUpToDate(Entity e){
    	return e!=null && e.lastUpdated == this.lastUpdated ;
    }
    
    public boolean entityIsUpToDate(String id){ return entityIsUpToDate(getEntity(id)) ; }
    

    /**
     * True if the given position q is "within range" (in close vicinity) of the
     * agent. (right now it is 0.4 distance unit)
     */
    public boolean withinRange(Vec3 q){
        return withinRange(q, IN_RANGE);
    }
    
    /**
     * True if the entity is "within range" (in close vicinity) of the
     * agent. (right now it is 0.4 distance unit)
     */
    public boolean withinRange(Entity e){
    	return e != null && withinRange(e.position);
    }
    
    public boolean withinRange(String id){ return withinRange(getEntity(id)) ; }
    
    private boolean withinRange(Vec3 destination, float range){
        return this.position != null && this.position.distanceSquared(destination) < range * range;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String sep = ", ";
        sb.append("BeliefState\n [ ");
        for (var e : entities.values()) {
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

    @Override
    public BeliefState setEnvironment(Environment e) {
        super.setEnvironment(e);
        mentalMap = new MentalMap(env().pathFinder);
        return this;
    }

    @Override
    public void updateState() {
        super.updateState();
    }
}
